package unwx.keyB.services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import unwx.keyB.domains.Article;
import unwx.keyB.dto.ArticleCreateRequest;
import unwx.keyB.dto.ArticleEditRequest;
import unwx.keyB.exceptions.rest.exceptions.BadRequestException;
import unwx.keyB.exceptions.rest.exceptions.ResourceNotFoundException;
import unwx.keyB.repositories.ArticleRepository;
import unwx.keyB.utils.Transliterator;
import unwx.keyB.validators.ArticleValidator;

import java.time.LocalDateTime;
import java.util.List;

@Service
@PropertySources({
        @PropertySource("classpath:valid.properties"),
        @PropertySource("classpath:uri.properties")
})
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final Transliterator transliterator;
    private final ArticleValidator articleValidator;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, Transliterator transliterator, ArticleValidator articleValidator) {
        this.articleRepository = articleRepository;
        this.transliterator = transliterator;
        this.articleValidator = articleValidator;
    }


    @Nullable
    public List<Article> getAll() {
        return articleRepository.findAll();
    }

    public ResponseEntity<Article> getById(Long id) {
        Article article = articleRepository.getOne(id);
        if (article.getId() != null)
            return new ResponseEntity<>(article, HttpStatus.OK);

        else throw new ResourceNotFoundException("article not found.");
    }

    public ResponseEntity<Article> createArticle(ArticleCreateRequest articleRequest) {
        if (articleValidator.isValidToCreate(articleRequest)) {
            Article article = new Article();
            article.setText(articleRequest.getText());
            article.setTitle(articleRequest.getTitle());
            return new ResponseEntity<>(fillInArticleAttributes_AndSave(article), HttpStatus.OK);
        }

        else throw new BadRequestException("validation error.");
    }

    public ResponseEntity<Article> editArticle(ArticleEditRequest articleRequest) {
        if (articleValidator.isValidToEdit(articleRequest)) {
            Article article = new Article();
            article.setId(articleRequest.getTargetId());
            article.setText(articleRequest.getText());
            article.setTitle(articleRequest.getTitle());
            return new ResponseEntity<>(fillInArticleAttributes_AndSave(article), HttpStatus.OK);
        }

        else throw new BadRequestException("validation error.");
    }


    public HttpStatus delete(@NotNull Article article) {
        try {
            articleRepository.delete(article);
        } catch (RuntimeException e) {
            throw new BadRequestException("error during deleting.");
        }
        return HttpStatus.OK;
    }

    private Article fillInArticleAttributes_AndSave(@NotNull Article article) {
        article.setLink(transliterator.transliterate(article.getTitle()));
        article.setCreationDate(LocalDateTime.now());
        return articleRepository.save(article);
    }

}
