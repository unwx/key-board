package unwx.keyB.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.BeanUtils;
import unwx.keyB.domains.Article;
import unwx.keyB.services.ArticleService;
import unwx.keyB.validators.ArticleValidator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/article")
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
    public List<Article> getAll(){
         return articleService.getAll();
    }

    @GetMapping("{id}")
    public Article getOne(@PathVariable("id") Long id){
        Article article = articleService.getById(id);
        if (article.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else {
            return article;
        }
    }

    @PostMapping()
    public Article create(@RequestBody Article article){
        if (!articleValidator.isValid(article)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else {
            return articleService.save(article);
        }
    }

    @PutMapping("{id}")
    public Article edit(@PathVariable("id") Article target,
                        @Valid @RequestBody Article article,
                        BindingResult bindingResult){
        if (!articleValidator.isValid(article)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else {
            BeanUtils.copyProperties(article, target, "id");
            return articleService.save(article);
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Article article){
        articleService.delete(article);
    }

}
