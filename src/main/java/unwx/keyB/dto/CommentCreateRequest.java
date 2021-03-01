package unwx.keyB.dto;

public class CommentCreateRequest extends CommentRequest{

    private final Long articleId;

    public CommentCreateRequest(String text,
                                Long articleId) {
        super(text);
        this.articleId = articleId;
    }

    public Long getArticleId() {
        return articleId;
    }

}
