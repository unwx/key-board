package unwx.keyB.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import unwx.keyB.domains.Role;
import unwx.keyB.domains.User;
import unwx.keyB.dto.ClaimsDto;
import unwx.keyB.dto.JwtDto;
import unwx.keyB.dto.UserLoginRequest;
import unwx.keyB.dto.UserRegistrationRequest;
import unwx.keyB.exceptions.rest.exceptions.BadRequestException;
import unwx.keyB.exceptions.rest.exceptions.ResourceNotFoundException;
import unwx.keyB.repositories.UserRepository;
import unwx.keyB.security.jwt.token.JWTokenData;
import unwx.keyB.security.jwt.token.JwtTokenProvider;
import unwx.keyB.validators.UserValidator;

import javax.servlet.ServletRequest;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserValidator validator;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


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

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public User refreshTokens(User user) {
        return userRepository.save(updateUserToken(user));
    }

    public ResponseEntity<User> login(UserLoginRequest user) {
        if (validator.isValidLogin(user)) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(), user.getPassword()));

            User userFromDb = userRepository.findByUsername(user.getUsername());
            if (userFromDb == null) {
                throw new UsernameNotFoundException("User " + user.getUsername() + " not found.");
            } else {
                User userWithUpdatedTokens = refreshTokens(userFromDb);
                return new ResponseEntity<>(userWithUpdatedTokens, HttpStatus.OK);
            }

        } else throw new BadRequestException("invalid user.");
    }

    public ResponseEntity<User> registration(UserRegistrationRequest user) {
        if (validator.isValidRegistration(user)) {
            User createdUser = register(user);
            return new ResponseEntity<>(createdUser, HttpStatus.OK);

        } else throw new BadRequestException("invalid user.");
    }

    public ResponseEntity<JwtDto> refreshTokens(ServletRequest requestWithRefreshToken) {
        ClaimsDto claims = (ClaimsDto) requestWithRefreshToken.getAttribute("claims");
        if (claims != null) {
            User user = userRepository.findByUsername(claims.getClaims().get("sub").asString());
            if (user != null) {
                User userWithUpdatedTokens = updateUserToken(user);

                JwtDto tokens = new JwtDto(
                        userWithUpdatedTokens.getAccessToken(),
                        userWithUpdatedTokens.getRefreshToken());

                return new ResponseEntity<>(tokens, HttpStatus.OK);
            }
        }
        throw new BadRequestException("error during refresh.");
    }

    private User register(UserRegistrationRequest userRegistrationRequest) {

        User isDuplicate = userRepository.findByUsername(userRegistrationRequest.getUsername());
        if (isDuplicate == null) {
            User user = new User();
            user.setEmail(userRegistrationRequest.getEmail());
            user.setPassword(userRegistrationRequest.getPassword());
            user.setUsername(userRegistrationRequest.getUsername());
            user.setRoles(Collections.singleton(Role.USER));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setActive(true);
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
}
