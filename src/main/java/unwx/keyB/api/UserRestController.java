package unwx.keyB.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unwx.keyB.dto.UserChangeAvatarDto;
import unwx.keyB.security.jwt.JwtAuthenticationException;
import unwx.keyB.services.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }


    /**
     * @uri
     * /api/user/change/avatar
     *
     * @method
     * post
     *
     * @request
     * header: accessToken (Authorization:accessToken)
     * data: avatar (multipart/form-data)
     *
     * @response
     * (OK):
     * Response {
     *  avatarName: string
     * }
     *
     * (BadRequestException):
     * ErrorMessage {
     *      statusCode: int
     *      timestamp: string
     *      message: string
     *      description: string
     * }
     */
    @PostMapping("/change/avatar")
    public ResponseEntity<UserChangeAvatarDto> changeAvatar(@RequestHeader(value = "Authorization") String accessToken,
                                                            @RequestParam MultipartFile avatar) throws IOException, JwtAuthenticationException {
        return userService.changeAvatar(accessToken, avatar);
    }
}
