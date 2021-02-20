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
import unwx.keyB.repositories.UserRepository;
import unwx.keyB.security.jwt.token.JwtTokenProvider;
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
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, Transliterator transliterator, ArticleValidator articleValidator, UserRepository userRepository, JwtTokenProvider tokenProvider) {
        this.articleRepository = articleRepository;
        this.transliterator = transliterator;
        this.articleValidator = articleValidator;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
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

    public ResponseEntity<Article> createArticle(ArticleCreateRequest articleRequest, String token) {
        return new ResponseEntity<>(createArticleProcess(articleRequest, token), HttpStatus.OK);
    }

    public ResponseEntity<Article> editArticle(ArticleEditRequest articleRequest, String token) {
        return new ResponseEntity<>(editArticleProcess(articleRequest, token), HttpStatus.OK);
    }

    public HttpStatus delete(long id, String token) {
        return deleteProcess(id, token);
    }

    private Article createArticleProcess(ArticleCreateRequest articleRequest, String token) {
        if (articleValidator.isValidToCreate(articleRequest)) {
            Article article = new Article();
            article.setText(articleRequest.getText());
            article.setTitle(articleRequest.getTitle());
            article.setLikes(0);
            article.setAuthor(userRepository
                    .findByUsername(tokenProvider
                            .getUsername(token)));

            return fillInArticleAttributes_AndSave(article);
        } else throw new BadRequestException("validation error.");
    }

    private Article editArticleProcess(ArticleEditRequest articleRequest, String token) {
        if (articleValidator.isValidToEdit(articleRequest) && isRealAuthor(articleRequest, token)) {
            Article article = new Article();
            article.setId(articleRequest.getTargetId());
            article.setText(articleRequest.getText());
            article.setTitle(articleRequest.getTitle());
            return fillInArticleAttributes_AndSave(article);
        }
        throw new BadRequestException("validation error.");
    }

    private HttpStatus deleteProcess(long id, String token) {
        if (id > -1) {
            Article article = articleRepository
                    .findById(id)
                    .orElseThrow(() -> new BadRequestException("article doesn't exist"));
            if (article.getAuthor().getUsername().equals(tokenProvider.getUsername(token))) {
                articleRepository.delete(article);
                return HttpStatus.OK;
            }
        }
        throw new BadRequestException("invalid id.");
    }

    private Article fillInArticleAttributes_AndSave(@NotNull Article article) {
        article.setLink(transliterator.transliterate(article.getTitle()));
        article.setCreationDate(LocalDateTime.now());
        return articleRepository.save(article);
    }

    private boolean isRealAuthor(ArticleEditRequest request, String token) {
        String authorUsername = articleRepository
                .findById(request.getTargetId())
                .orElseThrow(() -> new BadRequestException("article doesn't exist.")).getAuthor().getUsername();

        return authorUsername.equals(tokenProvider.getUsername(token));
    }

}
