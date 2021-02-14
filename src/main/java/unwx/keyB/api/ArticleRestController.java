package unwx.keyB.api;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unwx.keyB.domains.Article;
import unwx.keyB.exceptions.rest.exceptions.BadRequestException;
import unwx.keyB.exceptions.rest.exceptions.ResourceNotFoundException;
import unwx.keyB.services.ArticleService;
import unwx.keyB.validators.ArticleValidator;

import java.util.List;

@RestController
@RequestMapping("/api/article")
@PreAuthorize("hasAuthority('USER')")
public class ArticleRestController {

    private final ArticleService articleService;
    private final ArticleValidator articleValidator;

    @Autowired
    public ArticleRestController(ArticleService articleService, ArticleValidator articleValidator) {
        this.articleService = articleService;
        this.articleValidator = articleValidator;
    }

    // TODO : start index, response size; (get page)
    @GetMapping()
    public ResponseEntity<List<Article>> getAll(){
         return new ResponseEntity<>(articleService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Article> getOne(@PathVariable("id") Long id){
        Article article = articleService.getById(id);
        if (article.getId() == null) {
            throw new ResourceNotFoundException("article not found.");
        }
        else {
            return new ResponseEntity<>(article, HttpStatus.OK);
        }
    }

    @PostMapping()
    public ResponseEntity<Article> create(@RequestBody Article article){
        if (!articleValidator.isValid(article)) {
            throw new BadRequestException("validation error.");
        }
        else {
            return new ResponseEntity<>(articleService.save(article), HttpStatus.OK);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Article> edit(@PathVariable("id") Article target,
                                       @RequestBody Article article){
        if (!articleValidator.isValid(article)) {
            throw new BadRequestException("validation error.");
        }
        else {
            BeanUtils.copyProperties(article, target, "id");
            return new ResponseEntity<>(articleService.save(article), HttpStatus.OK);
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Article article){
        articleService.delete(article);
    }

}
