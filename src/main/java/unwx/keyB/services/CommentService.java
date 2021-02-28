package unwx.keyB.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import unwx.keyB.dao.ArticleDao;
import unwx.keyB.dao.CommentDao;
import unwx.keyB.dao.UserDao;
import unwx.keyB.dao.entities.DeleteType;
import unwx.keyB.dao.entities.SaveType;
import unwx.keyB.dao.sql.entities.DatabaseTable;
import unwx.keyB.dao.sql.entities.SqlField;
import unwx.keyB.dao.sql.entities.SqlTableRequest;
import unwx.keyB.domains.Article;
import unwx.keyB.domains.Comment;
import unwx.keyB.domains.User;
import unwx.keyB.dto.CommentCreateRequest;
import unwx.keyB.dto.CommentEditRequest;
import unwx.keyB.exceptions.rest.exceptions.BadRequestException;
import unwx.keyB.security.jwt.token.JwtTokenProvider;
import unwx.keyB.validators.CommentValidator;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CommentService {

    private final CommentValidator commentValidator;
    private final JwtTokenProvider tokenProvider;
    private final CommentDao commentDao = new CommentDao();
    private final UserDao userDao = new UserDao();
    private final ArticleDao articleDao = new ArticleDao();

    public CommentService(CommentValidator commentValidator,
                          JwtTokenProvider provider) {
        this.commentValidator = commentValidator;
        this.tokenProvider = provider;
    }

    public ResponseEntity<Comment> createComment(CommentCreateRequest request, String token) {
        return new ResponseEntity<>(createCommentProcess(request, token), HttpStatus.OK);
    }

    public ResponseEntity<Comment> editComment(CommentEditRequest request, String token) {
        return new ResponseEntity<>(editCommentProcess(request, token), HttpStatus.OK);
    }

    public HttpStatus deleteComment(long id, String token) {
        return deleteCommentProcess(id, token);
    }

    private HttpStatus deleteCommentProcess(long id, String token) {
        if (id > -1) {
            if (isRealAuthor(new CommentEditRequest(null, id), tokenProvider.resolveToken(token))) {
                commentDao.delete(new SqlField(id, "id"), DeleteType.SINGLE);
                return HttpStatus.OK;
            }
        }
        throw new BadRequestException("invalid id");
    }

    private Comment createCommentProcess(CommentCreateRequest request, String token) {
        String cleanToken = tokenProvider.resolveToken(token);

        if (commentValidator.isValidToCreate(request)) {
            Comment toCreate = new Comment.Builder()
                    .text(request.getText())
                    .likes(0)
                    .build();
            getAndSetNestedAttributes(request, toCreate, cleanToken);

            Long createdId = commentDao.save(toCreate, SaveType.CREATE);
            return commentDao.readEager(
                    createdId,
                    getCommentSqlColumnsDefault(),
                    Collections.singletonList(getAuthorSqlRequestDefault()),
                    new SqlField(createdId, "id"));
        }
        throw new BadRequestException("invalid comment.");
    }

    private Comment editCommentProcess(CommentEditRequest request, String token) {
        if (commentValidator.isValidToEdit(request) && isRealAuthor(request, token)) {
            Comment comment = new Comment.Builder()
                    .id(request.getCommentId())
                    .text(request.getText())
                    .build();
            commentDao.save(comment, SaveType.UPDATE);

            return commentDao.readEager(
                    request.getCommentId(),
                    getCommentSqlColumnsDefault(),
                    Collections.singletonList(getAuthorSqlRequestDefault()),
                    new SqlField(request.getCommentId(), "id"));
        }
        throw new BadRequestException("invalid comment.");
    }

    private void getAndSetNestedAttributes(CommentCreateRequest request,
                                           Comment target,
                                           String cleanToken) {
        String authorUsername = tokenProvider.getUsername(cleanToken);
        User author = userDao.readLazy(
                Collections.singletonList("id"),
                new SqlField(authorUsername, "username"));

        // check existence
        Article article = articleDao.readLazy(
                Collections.singletonList("id"),
                new SqlField(request.getArticleId(), "id"));


        target.setArticle(article);
        target.setAuthor(author);
    }

    private boolean isRealAuthor(CommentEditRequest request, String cleanToken) {
        String tokenUsername = tokenProvider.getUsername(cleanToken);
        String authorUsername = commentDao.readLinkedEntity(
                commentDao.readLazy(
                        Collections.singletonList("user_id"),
                        new SqlField(request.getCommentId(), "id")).getAuthor().getId(),
                new SqlTableRequest(
                        DatabaseTable.USER,
                        Collections.singletonList("username"),
                        false, "user_id")
        ).getAuthor().getUsername();

        return tokenUsername.equals(authorUsername);
    }

    private List<String> getCommentSqlColumnsDefault() {
        return new Comment.Columns()
                .id()
                .text()
                .likes()
                .get();
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
                false,
                "user_id");
    }
}
