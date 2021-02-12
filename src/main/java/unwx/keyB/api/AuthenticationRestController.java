package unwx.keyB.api;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import unwx.keyB.dto.AuthenticationRequestDto;
import unwx.keyB.dto.AuthenticationResponseDto;
import unwx.keyB.security.jwt.token.JwtTokenProvider;
import unwx.keyB.services.UserService;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;


    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }


    @PostMapping("login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody AuthenticationRequestDto requestDto) throws JsonProcessingException {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }

        String token = jwtTokenProvider.create(user);
        return new ResponseEntity<>(new AuthenticationResponseDto(username, token), HttpStatus.OK);
    }

    @PostMapping("registration")
    public ResponseEntity<AuthenticationResponseDto> registration(@RequestBody User user) {
        User createdUser = userService.register(user);
        String token = jwtTokenProvider.create(user);
        return new ResponseEntity<>(new AuthenticationResponseDto(createdUser.getUsername(), token), HttpStatus.OK);
    }
}
