package unwx.keyB.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unwx.keyB.domains.Comment;
import unwx.keyB.dto.CommentCreateRequest;
import unwx.keyB.dto.CommentEditRequest;
import unwx.keyB.dto.CommentGetFromArticleRequest;
import unwx.keyB.dto.CommentGetFromUserRequest;
import unwx.keyB.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@PreAuthorize("hasAuthority('USER')")
/*
   access token required.
   headers: {Authorization:Bearer_{access token}}
 */
public class CommentRestController {

    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * @uri
     * /api/comment/get/user
     *
     * @method
     * get
     *
     * @request
     * Request {
     *     user_id: long
     *     size: short
     * }
     *
     * @response
     * (OK):
     * [
     *      Comment {
     *          id: long;
     *          text: string;
     *          likes: int;
     *      }
     *      ...
     *  ]
     *
     * (Exception):
     * ErrorMessage {
     *      statusCode: int
     *      timestamp: string
     *      message: string
     *      description: string
     * }
     */
    @GetMapping("/get/user")
    public ResponseEntity<List<Comment>> getUserComments(@RequestBody CommentGetFromUserRequest request) {
        return commentService.getUsersComments(request);
    }

    /**
     * @uri
     * /api/comment/get/article
     *
     * @method
     * get
     *
     * @request
     * Request {
     *     article_id: long
     *     size: short
     * }
     *
     * @response
     * (OK):
     * [
     *      Comment {
     *          id: long;
     *          text: string;
     *          likes: int;
     *          Author {
     *              id: long
     *              username: string
     *              avatarName: string;
     *          }
     *      }
     *      ...
     *  ]
     *
     * (Exception):
     * ErrorMessage {
     *      statusCode: int
     *      timestamp: string
     *      message: string
     *      description: string
     * }
     */
    @GetMapping("/get/article")
    public ResponseEntity<List<Comment>> getArticleComments(@RequestBody CommentGetFromArticleRequest request) {
        return commentService.getArticleComments(request);
    }


    /**
     * @uri
     * /api/comment/create
     *
     * @method
     * post
     *
     * @request
     * Comment {
     *     text: string|MAX_TEXT_LENGTH: 1000; MIN_TEXT_LENGTH: 2|
     *     articleId: long;
     * }
     *
     * @response
     * (OK):
     *  Comment {
     *      id: long;
     *      text: string;
     *      likes: int;
     *      author {
     *          username: string
     *          avatarName: string
     *      }
     *  }
     *
     * (Exception):
     * ErrorMessage {
     *      statusCode: int
     *      timestamp: string
     *      message: string
     *      description: string
     * }
     */
    @PostMapping("/create")
    public ResponseEntity<Comment> createComment(@RequestBody CommentCreateRequest request,
                                                 @RequestHeader("Authorization") String accessToken) {
        return commentService.createComment(request, accessToken);
    }

    /**
     * @uri
     * /api/comment/edit
     *
     * @method
     * put
     *
     * @request
     * Comment {
     *     text: string|MAX_TEXT_LENGTH: 1000; MIN_TEXT_LENGTH: 2|
     *     commentId: long;
     * }
     *
     * @response
     * (OK):
     *  Comment {
     *      id: long;
     *      text: string;
     *      likes: int;
     *      author {
     *          username: string
     *          avatarName: string
     *      }
     *  }
     *
     * (Exception):
     * ErrorMessage {
     *      statusCode: int
     *      timestamp: string
     *      message: string
     *      description: string
     * }
     */
    @PutMapping("/edit")
    public ResponseEntity<Comment> editComment(@RequestBody CommentEditRequest request,
                                               @RequestHeader("Authorization") String accessToken) {
        return commentService.editComment(request, accessToken);
    }

    /**
     * @uri
     * /api/comment/delete
     *
     * @method
     * delete
     *
     * @request
     * id: long
     *
     * @response
     * (OK):
     * httpStatus: 200
     *
     * (Exception):
     * ErrorMessage {
     *      statusCode: int
     *      timestamp: string
     *      message: string
     *      description: string
     * }
     */
    @DeleteMapping("/delete")
    public HttpStatus deleteComment(@RequestBody long id,
                                    @RequestHeader("Authorization") String accessToken) {
        return commentService.deleteComment(id, accessToken);
    }
}
