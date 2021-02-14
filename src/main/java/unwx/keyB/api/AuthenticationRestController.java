package unwx.keyB.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unwx.keyB.domains.User;
import unwx.keyB.dto.AuthenticationResponseDto;
import unwx.keyB.dto.ClaimsDto;
import unwx.keyB.dto.JwtDto;
import unwx.keyB.exceptions.rest.exceptions.BadRequestException;
import unwx.keyB.security.jwt.token.JwtTokenProvider;
import unwx.keyB.services.UserService;
import unwx.keyB.validators.UserValidator;

import javax.servlet.ServletRequest;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserValidator validator;


    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, UserValidator validator) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.validator = validator;
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody User user) {
        if (!validator.isValidLogin(user)) {
            throw new BadRequestException("invalid user.");
        }
        else {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            User userFromDb = userService.findByUsername(user.getUsername());
            if (userFromDb == null) {
                throw new UsernameNotFoundException("User " + user.getUsername() + " not found.");
            }
            else {
                User userWithUpdatedTokens = userService.refreshTokens(user, jwtTokenProvider);
                return new ResponseEntity<>(new AuthenticationResponseDto(userWithUpdatedTokens), HttpStatus.OK);
            }
        }
        
    }

    @PostMapping("registration")
    public ResponseEntity<AuthenticationResponseDto> registration(@RequestBody User user) {
        if (!validator.isValidRegistration(user)) {
            throw new BadRequestException("invalid user.");
        }
        else {
            User createdUser = userService.register(user, jwtTokenProvider);
            return new ResponseEntity<>(new AuthenticationResponseDto(createdUser), HttpStatus.OK);
        }
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtDto> refresh(ServletRequest servletRequest) {
        ClaimsDto claims = (ClaimsDto) servletRequest.getAttribute("claims");
        if (claims != null) {
            User user = userService.findByUsername(claims.getClaims().get("sub").asString());
            if (user != null) {
                User userWithUpdatedTokens = userService.refreshTokens(user, jwtTokenProvider);

                JwtDto tokens = new JwtDto(
                        userWithUpdatedTokens.getAccessToken(),
                        userWithUpdatedTokens.getRefreshToken());

                return new ResponseEntity<>(tokens, HttpStatus.OK);
            }
        }
        throw new BadRequestException("error during refresh.");
    }
}
