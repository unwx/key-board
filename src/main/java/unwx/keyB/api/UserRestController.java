package unwx.keyB.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unwx.keyB.domains.User;
import unwx.keyB.security.jwt.JwtAuthenticationException;
import unwx.keyB.services.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAuthority('USER')")
/*
   access token required.
   headers: {Authorization:Bearer_{access token}}
 */
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
     *  avatar_name: string
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
    public ResponseEntity<User> changeAvatar(@RequestHeader(value = "Authorization") String accessToken,
                                             @RequestParam MultipartFile avatar) throws IOException, JwtAuthenticationException {
        return userService.changeAvatar(accessToken, avatar);
    }

    /**
     * @uri
     * /api/user/avatar/{avatar_name}
     *
     * @method
     * get
     *
     * @request
     * header: accessToken (Authorization:accessToken)
     * path_variable: avatarName
     *
     * @response
     * (OK):
     * Response: int[] {-1, -40, -1, -32, 0, 74...}
     *
     * (BadRequestException | InternalException):
     * ErrorMessage {
     *      statusCode: int
     *      timestamp: string
     *      message: string
     *      description: string
     * }
     */
    @GetMapping(value = "/avatar/{avatar_name}")
    public ResponseEntity<int[]> getAvatar(@PathVariable("avatar_name") String avatarName) {
        return userService.getAvatar(avatarName);
    }

}
