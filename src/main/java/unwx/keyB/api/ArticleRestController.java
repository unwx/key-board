package unwx.keyB.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.BeanUtils;
import unwx.keyB.domains.Article;
import unwx.keyB.services.ArticleService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleRestController {

    private final ArticleService articleService;

    @Autowired
    public ArticleRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // TODO : start index, response size; (get page)
    @GetMapping()
    public List<Article> getAll(){
        return articleService.getAll();
    }

    @GetMapping("{id}")
    public Article getOne(@PathVariable("id") Long id){
        Article article = articleService.getById(id);
        if (article.getId() == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return article;
    }

    @PostMapping()
    public Article create(@Valid @RequestBody Article article, BindingResult bindingResult){
        validateProcess(bindingResult);
        return articleService.save(article);
    }

    @PutMapping("{id}")
    public Article edit(@PathVariable("id") Article target,
                        @Valid @RequestBody Article article,
                        BindingResult bindingResult){
        validateProcess(bindingResult);

        BeanUtils.copyProperties(article, target, "id");
        return articleService.save(article);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Article article){
        articleService.delete(article);
    }

    private void validateProcess(BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

}
