package unwx.keyB.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unwx.keyB.domains.User;
import unwx.keyB.dto.JwtDto;
import unwx.keyB.dto.UserLoginRequest;
import unwx.keyB.dto.UserRegistrationRequest;
import unwx.keyB.services.UserService;

import javax.servlet.ServletRequest;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationRestController {

    private final UserService userService;

    @Autowired
    public AuthenticationRestController(UserService userService) {
        this.userService = userService;
    }


    /**
     * @uri
     * /api/auth/login
     *
     * @method
     * post
     *
     * @request
     * User {
     *     username: string |USERNAME_MIN: 2; USERNAME_MAX: 15|
     *     password: string |PASSWORD_MIN: 8; PASSWORD_MAX: 30|
     * }
     *
     * @response
     * (OK):
     *  User {
     *      id: long
     *      username: string
     *      email: string
     *      accessToken: string
     *      refreshToken: string
     *      avatar_name: string
     *  }
     *
     * (BadRequestException):
     * ErrorMessage {
     *      statusCode: int
     *      timestamp: string
     *      message: string
     *      description: string
     * }
     */
    @PostMapping("login")
    public ResponseEntity<User> login(@RequestBody UserLoginRequest user) {
        return userService.login(user);
    }

    /**
     * @uri
     * /api/auth/registration
     *
     * @method
     * post
     *
     * @request
     * User {
     *     username: string |USERNAME_MIN: 2; USERNAME_MAX: 15|
     *     password: string |PASSWORD_MIN: 8; PASSWORD_MAX: 30|
     *     email: string |EMAIL_MIN: 5; EMAIL_MAX: 64|
     * }
     *
     * @response
     * (OK):
     *  User {
     *      id: long
     *      username: string
     *      email: string
     *      accessToken: string
     *      refreshToken: string
     *      avatar_name: string
     *  }
     *
     * (BadRequestException):
     * ErrorMessage {
     *      statusCode: int
     *      timestamp: string
     *      message: string
     *      description: string
     * }
     */
    @PostMapping("registration")
    public ResponseEntity<User> registration(@RequestBody UserRegistrationRequest user) {
        return userService.registration(user);
    }

    /**
     * @uri
     * /api/auth/refresh
     *
     * @method
     * post
     *
     * @request
     * header: refreshToken
     * body: -
     *
     * @response
     * JWT {
     *     accessToken: string
     *     refreshToken: string
     * }
     */
    @PostMapping("refresh")
    public ResponseEntity<JwtDto> refresh(ServletRequest servletRequest) {
        return userService.refreshTokens(servletRequest);
    }
}
