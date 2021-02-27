package unwx.keyB.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import unwx.keyB.dao.ArticleDao;
import unwx.keyB.dao.UserDao;
import unwx.keyB.dao.entities.DeleteType;
import unwx.keyB.dao.entities.SaveType;
import unwx.keyB.dao.sql.entities.DatabaseTable;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.entities.SqlTableRequest;
import unwx.keyB.domains.Article;
import unwx.keyB.domains.User;
import unwx.keyB.dto.ArticleCreateRequest;
import unwx.keyB.dto.ArticleEditRequest;
import unwx.keyB.exceptions.rest.exceptions.BadRequestException;
import unwx.keyB.security.jwt.token.JwtTokenProvider;
import unwx.keyB.utils.Transliterator;
import unwx.keyB.validators.ArticleValidator;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@PropertySources({
        @PropertySource("classpath:valid.properties"),
        @PropertySource("classpath:uri.properties")
})
public class ArticleService {

    private final Transliterator transliterator;
    private final ArticleValidator articleValidator;
    private final JwtTokenProvider tokenProvider;
    private final ArticleDao articleDao = new ArticleDao();
    private final UserDao userDao = new UserDao();

    @Autowired
    public ArticleService(Transliterator transliterator,
                          ArticleValidator articleValidator,
                          JwtTokenProvider tokenProvider) {
        this.transliterator = transliterator;
        this.articleValidator = articleValidator;
        this.tokenProvider = tokenProvider;
    }

    public ResponseEntity<List<Article>> get(int start, short length) {
        return new ResponseEntity<>(
                articleDao.readManyLazy(getArticleSqlColumnsDefault(),
                        "`id` > " + start,
                        length),
                HttpStatus.OK);
    }

    public ResponseEntity<Article> getById(Long id) {
        return new ResponseEntity<>(
                articleDao.readLazy(getArticleSqlColumnsDefault(),
                        new SqlField(id, "id")
                ), HttpStatus.OK);
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
        String cleanToken = tokenProvider.resolveToken(token);
        if (articleValidator.isValidToCreate(articleRequest)) {
            User author = userDao.readLazy(
                    Collections.singletonList("id"),
                    new SqlField("username", tokenProvider.getUsername(cleanToken)));

            Article toCreate = new Article.Builder()
                    .text(articleRequest.getText())
                    .title(articleRequest.getTitle())
                    .author(author)
                    .link(resolveLink(articleRequest.getTitle()))
                    .creationDate(LocalDateTime.now())
                    .likes(0)
                    .build();

            Long createdId = articleDao.save(toCreate, SaveType.CREATE);

            return articleDao.readEager(
                    createdId,
                    getArticleSqlColumnsDefault(),
                    Collections.singletonList(getAuthorSqlRequestDefault()),
                    new SqlField(createdId, "id"));

        }
        throw new BadRequestException("validation error.");
    }

    private Article editArticleProcess(ArticleEditRequest articleRequest, String token) {
        String cleanToken = tokenProvider.resolveToken(token);
        if (articleValidator.isValidToEdit(articleRequest) && isRealAuthor(articleRequest, cleanToken)) {
            Article toUpdate = new Article.Builder()
                    .text(articleRequest.getText())
                    .title(articleRequest.getTitle())
                    .link(resolveLink(articleRequest.getTitle()))
                    .build();
            articleDao.save(toUpdate, SaveType.UPDATE);

            return articleDao.readEager(
                    articleRequest.getTargetId(),
                    getArticleSqlColumnsDefault(),
                    Collections.singletonList(getAuthorSqlRequestDefault()),
                    new SqlField(articleRequest.getTargetId(), "id"));

        }
        throw new BadRequestException("validation error.");
    }

    private HttpStatus deleteProcess(long id, String token) {
        String cleanToken = tokenProvider.resolveToken(token);
        if (id > -1) {
            User user = userDao.readLazy(
                    Collections.singletonList("id"),
                    new SqlField(tokenProvider.resolveToken(cleanToken), "username"));
            Article article = articleDao.readEager(
                    user.getId(),
                    Collections.singletonList("id"),
                    Collections.singletonList(new SqlTableRequest(
                            DatabaseTable.USER,
                            Collections.singletonList("username"),
                            (short) 1,
                            false)),
                    new SqlField(id, "id"));
            if (article.getAuthor().getUsername().equals(user.getUsername())) {
                articleDao.delete(new SqlField(id, "id"), DeleteType.ORPHAN_REMOVAL);
                return HttpStatus.OK;
            }
            throw new AccessDeniedException("forbidden.");
        }
        throw new BadRequestException("invalid id.");
    }

    private String resolveLink(@NotNull String title) {
        return transliterator.transliterate(title);
    }

    private boolean isRealAuthor(ArticleEditRequest request, String token) {
        String tokenUsername = tokenProvider.getUsername(tokenProvider.resolveToken(token));

        String authorUsername = articleDao.readLinkedEntity(
                articleDao.readLazy(
                        Collections.singletonList("user_id"),
                        new SqlField(request.getTargetId(), "id")).getAuthor().getId(),
                new SqlTableRequest(
                        DatabaseTable.USER,
                        Collections.singletonList("username"),
                        false)
        ).getAuthor().getUsername();

        return tokenUsername.equals(authorUsername);
    }

    private SqlTableRequest getAuthorSqlRequestDefault() {
        return new SqlTableRequest(
                DatabaseTable.USER,
                new ArrayList<>() {

                    @Serial
                    private static final long serialVersionUID = 6524590556519986636L;

                    {
                        add("id");
                        add("avatar_name");
                        add("username");
                    }
                },
                (short) 1,
                false);
    }

    private List<String> getArticleSqlColumnsDefault() {
        return new Article.Columns()
                .id()
                .text()
                .title()
                .creationDate()
                .likes()
                .link()
                .get();
    }
}
