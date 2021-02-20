package unwx.keyB.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import unwx.keyB.domains.Article;
import unwx.keyB.domains.Comment;
import unwx.keyB.domains.User;
import unwx.keyB.dto.CommentCreateRequest;
import unwx.keyB.dto.CommentEditRequest;
import unwx.keyB.exceptions.rest.exceptions.BadRequestException;
import unwx.keyB.repositories.ArticleRepository;
import unwx.keyB.repositories.CommentRepository;
import unwx.keyB.repositories.UserRepository;
import unwx.keyB.security.jwt.token.JwtTokenProvider;
import unwx.keyB.validators.CommentValidator;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentValidator commentValidator;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    public CommentService(CommentRepository commentRepository, CommentValidator commentValidator, ArticleRepository articleRepository, UserRepository userRepository, JwtTokenProvider provider) {
        this.commentRepository = commentRepository;
        this.commentValidator = commentValidator;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
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
            Comment comment = commentRepository
                    .findById(id)
                    .orElseThrow(() -> new BadRequestException("comment doesn't exist"));
            if (comment.getAuthor().getUsername().equals(tokenProvider.getUsername(token))) {
                commentRepository.delete(comment);
                return HttpStatus.OK;
            }
        }
        throw new BadRequestException("invalid id");
    }

    private Comment createCommentProcess(CommentCreateRequest request, String token) {
        if (commentValidator.isValidToCreate(request)) {
            Comment comment = new Comment();
            getAndSetNestedAttributes(request, comment, token);
            comment.setText(request.getText());
            comment.setLikes(0);
            Comment saved = commentRepository.save(comment);

            return new Comment.Builder()
                    .id(saved.getId())
                    .text(saved.getText())
                    .likes(saved.getLikes())
                    .author(
                            new User.Builder()
                                    .username(saved.getAuthor().getUsername())
                                    .avatarName(saved.getAuthor().getAvatarName())
                                    .build())
                    .build();
        }
        throw new BadRequestException("invalid comment.");
    }

    private Comment editCommentProcess(CommentEditRequest request, String token) {
        if (commentValidator.isValidToEdit(request) && isRealAuthor(request, token)) {
            Comment comment = new Comment();
            comment.setId(request.getCommentId());
            comment.setText(request.getText());
            Comment saved = commentRepository.save(comment);

            return new Comment.Builder()
                    .id(saved.getId())
                    .text(saved.getText())
                    .likes(saved.getLikes())
                    .author(
                            new User.Builder()
                                    .username(saved.getAuthor().getUsername())
                                    .avatarName(saved.getAuthor().getAvatarName())
                                    .build())
                    .build();
        }
        throw new BadRequestException("invalid comment.");
    }

    private void getAndSetNestedAttributes(CommentCreateRequest request, Comment target, String token) {
        Article article = articleRepository
                .findById(request.getArticleId())
                .orElseThrow(() -> new BadRequestException("article doesn't exist."));
        User author = userRepository.findByUsername(tokenProvider.getUsername(token));
        if (author == null)
            throw new BadRequestException("user doesn't exist.");

        target.setArticle(article);
        target.setAuthor(author);
    }

    private boolean isRealAuthor(CommentEditRequest request, String token) {
        String tokenUsername = tokenProvider.getUsername(token);
        String authorUsername = commentRepository
                .findById(request.getCommentId())
                .orElseThrow(() -> new BadRequestException("comment doesn't exist."))
                .getAuthor().getUsername();

        return tokenUsername.equals(authorUsername);
    }
}
