package unwx.keyB.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
import unwx.keyB.dto.CommentGetFromArticleRequest;
import unwx.keyB.dto.CommentGetFromUserRequest;
import unwx.keyB.exceptions.internal.service.RequiredArgumentMissingException;
import unwx.keyB.exceptions.rest.exceptions.BadRequestException;
import unwx.keyB.exceptions.rest.exceptions.ResourceNotFoundException;
import unwx.keyB.security.jwt.token.JwtTokenProvider;
import unwx.keyB.validators.CommentValidator;
import unwx.keyB.validators.PieceOfInformationSelectValidator;

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
    private final PieceOfInformationSelectValidator pieceOfInformationSelectValidator = new PieceOfInformationSelectValidator();

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

    public ResponseEntity<List<Comment>> getUsersComments(CommentGetFromUserRequest request) {
        return new ResponseEntity<>(getUsersCommentsProcess(request), HttpStatus.OK);
    }

    public ResponseEntity<List<Comment>> getArticleComments(CommentGetFromArticleRequest request) {
        return new ResponseEntity<>(getArticleCommentsProcess(request), HttpStatus.OK);
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

    private List<Comment> getUsersCommentsProcess(CommentGetFromUserRequest request) {
        if (pieceOfInformationSelectValidator.isValid(request)) {
            List<Comment> comments = commentDao.readManyLazy(
                    getCommentSqlColumnsDefault(),
                    "`user_id`='" + request.getSelectIndex() + "'",
                    request.getSize());
            if (comments != null && !comments.isEmpty())
                return comments;
            throw new ResourceNotFoundException("user doesn't exist || comments not found.");
        }
        throw new BadRequestException("validation error.");
    }

    private List<Comment> getArticleCommentsProcess(CommentGetFromArticleRequest request) {
        if (pieceOfInformationSelectValidator.isValid(request)) {
            List<String> columns = getCommentSqlColumnsDefault();
            columns.add("user_id");

            List<Comment> comments = commentDao.readManyLazy(
                    columns,
                    "`article_id`='" + request.getSelectIndex() + "'",
                    request.getSize());

            if (comments == null || comments.isEmpty())
                throw new ResourceNotFoundException("article doesn't exist || comments not found.");

            List<Object> ids = new ArrayList<>(comments.size());
            comments.forEach((e) -> ids.add(e.getAuthor().getId()));

            List<Comment> linkedEntities = commentDao.readLinkedEntitiesManyToMany(ids, getAuthorSqlRequestDefault());

            for (int i = 0; i < comments.size(); i++) {
                comments.get(i).setAuthor(linkedEntities.get(i).getAuthor());
            }
            return comments;
        }
        throw new BadRequestException("validation error.");
    }

    private Comment createCommentProcess(CommentCreateRequest request, String token) {
        String cleanToken = tokenProvider.resolveToken(token);
        Long authorId;
        try {
            authorId = getAuthorIdFromToken(cleanToken);
        } catch (RequiredArgumentMissingException e) {
            throw new AccessDeniedException("account not found");
        }

        if (commentValidator.isValidToCreate(request)) {
            Comment toCreate = new Comment.Builder()
                    .text(request.getText())
                    .likes(0)
                    .build();

            try {
                getAndSetNestedAttributes(request, toCreate, cleanToken);
            } catch (RequiredArgumentMissingException e) {
                throw new BadRequestException(e.getMessage());
            }

            Long createdId = commentDao.save(toCreate, SaveType.CREATE);
            return commentDao.readEager(
                    authorId,
                    getCommentSqlColumnsDefault(),
                    Collections.singletonList(getAuthorSqlRequestDefault()),
                    new SqlField(createdId, "id"));
        }
        throw new BadRequestException("invalid comment.");
    }

    private Comment editCommentProcess(CommentEditRequest request, String token) {
        User author = getAuthorFromComment(request);

        if (commentValidator.isValidToEdit(request) && author != null && author.getId() != null
                && author.getUsername() != null &&
                author.getUsername().equals(tokenProvider.getUsername(tokenProvider.resolveToken(token)))) {

            Comment comment = new Comment.Builder()
                    .id(request.getCommentId())
                    .text(request.getText())
                    .build();
            commentDao.save(comment, SaveType.UPDATE);

            return commentDao.readEager(
                    author.getId(),
                    getCommentSqlColumnsDefault(),
                    Collections.singletonList(getAuthorSqlRequestDefault()),
                    new SqlField(author.getId(), "id"));
        }
        throw new BadRequestException("invalid comment.");
    }

    private void getAndSetNestedAttributes(CommentCreateRequest request,
                                           Comment target,
                                           String cleanToken) throws RequiredArgumentMissingException {
        String authorUsername = tokenProvider.getUsername(cleanToken);
        User author = userDao.readLazy(
                Collections.singletonList("id"),
                new SqlField(authorUsername, "username"));

        // check existence
        Article article = articleDao.readLazy(
                Collections.singletonList("id"),
                new SqlField(request.getArticleId(), "id"));

        if (author == null || article == null)
            throw new RequiredArgumentMissingException(
                    "some fields are incorrect: [author:" + author + " | article:" + article + "]");
        target.setArticle(article);
        target.setAuthor(author);
    }

    private boolean isRealAuthor(CommentEditRequest request, String token) {
        return tokenProvider.getUsername(tokenProvider.resolveToken(token))
                .equals(getAuthorFromComment(request).getUsername());
    }

    private User getAuthorFromComment(CommentEditRequest request) {
        List<String> columns = new ArrayList<>() {
            @Serial
            private static final long serialVersionUID = -9006666354293778442L;

            {
                add("username");
                add("id");
            }
        };

        Article temp = articleDao.readLazy(
                Collections.singletonList("user_id"),
                new SqlField(request.getCommentId(), "id"));

        if (temp != null)
            return articleDao.readLinkedEntity(
                    temp.getAuthor().getId(),
                    new SqlTableRequest(
                            DatabaseTable.USER,
                            columns,
                            false,
                            "user_id")
            ).getAuthor();
        throw new BadRequestException("comment not found.");
    }

    private Long getAuthorIdFromToken(String cleanToken) throws RequiredArgumentMissingException {
        User u = userDao.readLazy(
                Collections.singletonList("id"),
                new SqlField(tokenProvider.getUsername(cleanToken), "username"));
        if (u == null) {
            throw new RequiredArgumentMissingException("user from token doesn't exist.");
        }
        return u.getId();
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
