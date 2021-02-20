package unwx.keyB.services;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unwx.keyB.domains.Role;
import unwx.keyB.domains.User;
import unwx.keyB.dto.ClaimsDto;
import unwx.keyB.dto.JwtDto;
import unwx.keyB.dto.UserLoginRequest;
import unwx.keyB.dto.UserRegistrationRequest;
import unwx.keyB.exceptions.rest.exceptions.BadRequestException;
import unwx.keyB.exceptions.rest.exceptions.InternalException;
import unwx.keyB.repositories.UserRepository;
import unwx.keyB.security.jwt.JwtAuthenticationException;
import unwx.keyB.security.jwt.token.JWTokenData;
import unwx.keyB.security.jwt.token.JwtTokenProvider;
import unwx.keyB.utils.FileUtils;
import unwx.keyB.utils.ImageUtils;
import unwx.keyB.validators.UserValidator;

import javax.imageio.ImageIO;
import javax.servlet.ServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@PropertySource("classpath:files.properties")
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserValidator validator;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${file.user.upload-dir}")
    private String userAvatarsDir;

    @Value("${file.user-avatar-default}")
    private String userDefaultAvatarName;


    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       UserValidator validator,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User refreshTokens(User user) {
        return userRepository.save(updateUserToken(user));
    }

    public ResponseEntity<User> login(UserLoginRequest user) {
        return new ResponseEntity<>(loginProcess(user), HttpStatus.OK);
    }

    public ResponseEntity<User> registration(UserRegistrationRequest user) {
        return new ResponseEntity<>(registrationProcess(user), HttpStatus.OK);
    }

    public ResponseEntity<JwtDto> refreshTokens(ServletRequest requestWithRefreshToken) {
        return new ResponseEntity<>(refreshTokensProcess(requestWithRefreshToken), HttpStatus.OK);
    }

    public ResponseEntity<User> changeAvatar(String accessToken, MultipartFile avatar) throws IOException, JwtAuthenticationException {
        return new ResponseEntity<>(changeAvatarProcess(accessToken, avatar), HttpStatus.OK);
    }

    private User loginProcess(UserLoginRequest user) {
        if (validator.isValidLogin(user)) {
            authenticate(new User.Builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .build());

            User userFromDb = userRepository.findByUsername(user.getUsername());
            if (userFromDb != null) {
                User userWithUpdatedTokens = refreshTokens(userFromDb);
                return new User.Builder()
                        .username(userWithUpdatedTokens.getUsername())
                        .email(userWithUpdatedTokens.getEmail())
                        .accessToken(userWithUpdatedTokens.getAccessToken())
                        .refreshToken(userWithUpdatedTokens.getRefreshToken())
                        .avatarName(userWithUpdatedTokens.getAvatarName())
                        .build();
            }
        }
        throw new BadRequestException("invalid user.");
    }

    private User registrationProcess(UserRegistrationRequest user) {
        if (validator.isValidRegistration(user)) {
            User createdUser = register(user);
            return new User.Builder()
                    .username(createdUser.getUsername())
                    .email(createdUser.getEmail())
                    .accessToken(createdUser.getAccessToken())
                    .refreshToken(createdUser.getRefreshToken())
                    .build();

        } else throw new BadRequestException("invalid user.");
    }

    private JwtDto refreshTokensProcess(ServletRequest requestWithRefreshToken) {
        ClaimsDto claims = (ClaimsDto) requestWithRefreshToken.getAttribute("claims");
        if (claims != null) {
            User user = userRepository.findByUsername(claims.getClaims().get("sub").asString());
            if (user != null) {
                User userWithUpdatedTokens = updateUserToken(user);

                return new JwtDto(
                        userWithUpdatedTokens.getAccessToken(),
                        userWithUpdatedTokens.getRefreshToken());
            }
        }
        throw new BadRequestException("error during refresh.");
    }

    public ResponseEntity<int[]> getAvatar(String avatarName) {
        return new ResponseEntity<>(getAvatarProcess(avatarName), HttpStatus.OK);
    }

    private User changeAvatarProcess(String accessToken, MultipartFile avatar) throws IOException {
        String token = jwtTokenProvider.resolveToken(accessToken);
        if (token != null) {
            String username = jwtTokenProvider.getUsername(token);
            User user = userRepository.findByUsername(username);
            if (user != null) {
                String avatarName = avatarProcessAndSave(avatar, user);
                user.setAvatarName(avatarName);
                userRepository.save(user);
                return new User.Builder().avatarName(avatarName).build();
            }
        }
        throw new BadRequestException("user not found.");
    }

    private int[] getAvatarProcess(String avatarName) {
        if (validator.isAvatarName(avatarName)) {
            try {
                return FileUtils.loadFileAsResource(userAvatarsDir + avatarName);
            } catch (IOException e) {
                throw new InternalException("IO exception.");
            }
        }
        throw new BadRequestException("not an avatar.");
    }

    private User register(UserRegistrationRequest userRegistrationRequest) {
        User isDuplicate = userRepository.findByUsername(userRegistrationRequest.getUsername());
        if (isDuplicate == null) {
            User user = new User.Builder()
                    .email(userRegistrationRequest.getEmail())
                    .password(passwordEncoder.encode(userRegistrationRequest.getEmail()))
                    .username(userRegistrationRequest.getUsername())
                    .roles(Collections.singleton(Role.USER))
                    .active(true)
                    .avatarName(userDefaultAvatarName)
                    .build();
            authenticate(user);
            return userRepository.save(updateUserToken(user));
        } else throw new BadRequestException("User with this username already registered.");
    }

    private User updateUserToken(User user) {
        JWTokenData accessTokenData = jwtTokenProvider.createAccess(user);
        JWTokenData refreshTokenData = jwtTokenProvider.createRefresh(user);
        user.setAccessTokenExpiration(String.valueOf(accessTokenData.getExpirationAtMillis()));
        user.setRefreshTokenExpiration(String.valueOf(refreshTokenData.getExpirationAtMillis()));
        user.setAccessToken(accessTokenData.getToken());
        user.setRefreshToken(refreshTokenData.getToken());
        return user;
    }

    private String avatarProcessAndSave(MultipartFile avatar, User user) throws IOException {
        if (avatar != null && validator.isFilePicture(avatar)) {
            String extension = FileUtils.getFileExtension(avatar);
            if (!extension.isEmpty()) {
                BufferedImage thumbnail = Scalr.resize(
                        ImageUtils.getImageFromMultipartFile(avatar, extension),
                        Scalr.Mode.FIT_EXACT,
                        155);
                String avatarName = UUID.randomUUID().toString() + "."
                        + user.getUsername() + "."
                        + extension;

                String resultPath = userAvatarsDir + avatarName;
                File result = new File(resultPath);
                if (result.createNewFile()) {
                    ImageIO.write(thumbnail, extension, result);
                    ImageUtils.deleteSavedTempFile(extension);
                    deleteOldAvatarAfterUpdate(user);
                    return avatarName;
                }
            }
        }
        throw new BadRequestException("invalid image.");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteOldAvatarAfterUpdate(User user) {
        String avatarName = user.getAvatarName();
        if (avatarName != null && !avatarName.isEmpty()) {
            new File(userAvatarsDir + avatarName).delete();
        }
    }

    private void authenticate(User user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword()));
    }

}
