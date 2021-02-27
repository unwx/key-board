package unwx.keyB.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unwx.keyB.domains.Article;
import unwx.keyB.dto.ArticleCreateRequest;
import unwx.keyB.dto.ArticleEditRequest;
import unwx.keyB.services.ArticleService;

import java.util.List;

@RestController
@RequestMapping("/api/article")
@PreAuthorize("hasAuthority('USER')")
/*
   access token required.
   headers: {Authorization:Bearer_{access token}}
 */
public class ArticleRestController {

    private final ArticleService articleService;

    @Autowired
    public ArticleRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * @deprecated
     * @uri
     * /api/article
     *
     * @method
     * get
     *
     * @request
     * -
     *
     * @response
     * [
     *      Article {
     *          id: long
     *          title: string
     *          link: string
     *          text: string
     *          creationDate: string
     *      }
     *      ...
     * ]
     */
    @GetMapping()
    public ResponseEntity<List<Article>> getAll(){
         return articleService.get(0, (short) 50);
    }

    /**
     * @deprecated
     * @uri
     * /api/article/{id}
     *
     * @path-variable
     * id: long
     *
     * @method
     * get
     *
     * @request
     * -
     *
     * @response
     * (OK):
     * Article {
     *     id: long {id}
     *     title: string
     *     link: string
     *     text: string
     *     creationDate: string
     * }
     *
     * (ResourceNotFoundException):
     * ErrorMessage {
     *      statusCode: int
     *      timestamp: string
     *      message: string
     *      description: string
     * }
     */
    @GetMapping("{id}")
    public ResponseEntity<Article> getOne(@PathVariable("id") Long id){
        return articleService.getById(id);
    }


    /**
     * @uri
     * /api/article
     *
     * @method
     * post
     *
     * @request
     * Article {
     *      title: string |MIN_TITLE_LENGTH: 5; MAX_TITLE_LENGTH: 30|
     *      text: string |MIN_TEXT_LENGTH: 15; MAX_TEXT_LENGTH: 5000|
     * }
     *
     * @response
     * (OK):
     * Article {
     *     id: long
     *     title: string
     *     link: string
     *     text: string
     *     creationDate: string
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
    @PostMapping()
    public ResponseEntity<Article> create(@RequestBody ArticleCreateRequest article,
                                          @RequestHeader("Authorization") String accessToken){
        return articleService.createArticle(article, accessToken);
    }

    /**
     * @uri
     * /api/article
     *
     * @method
     * put
     *
     * @request
     * Article {
     *     id: long |should be correct, target to be changed|; target!
     *     title: string |replacement title; MIN_TITLE_LENGTH: 5; MAX_TITLE_LENGTH: 30|
     *     text: string |replacement text; MIN_TEXT_LENGTH: 15; MAX_TEXT_LENGTH: 5000|
     * }
     *
     * @response
     */
    @PutMapping()
    public ResponseEntity<Article> edit(@RequestBody ArticleEditRequest article,
                                        @RequestHeader("Authorization") String accessToken) {
        return articleService.editArticle(article, accessToken);
    }

    /**
     * @uri
     * /api/article/{id}
     *
     * @method
     * delete
     *
     * @request
     * -
     *
     * @response
     * httpStatus: httpStatus
     */
    @DeleteMapping("{id}")
    public HttpStatus delete(@PathVariable("id") long id,
                             @RequestHeader("Authorization") String accessToken){
        return articleService.delete(id, accessToken);
    }

}
