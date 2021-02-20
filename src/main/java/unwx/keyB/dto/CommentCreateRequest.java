package unwx.keyB.dto;

public class CommentCreateRequest extends CommentRequest{

    private final long articleId;

    public CommentCreateRequest(String text,
                                long articleId) {
        super(text);
        this.articleId = articleId;
    }

    public long getArticleId() {
        return articleId;
    }

}
