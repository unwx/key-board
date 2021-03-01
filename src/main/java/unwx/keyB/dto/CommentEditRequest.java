package unwx.keyB.dto;

public class CommentEditRequest extends CommentRequest {

    private final Long commentId;

    public CommentEditRequest(String text,
                              Long commentId) {
        super(text);
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }
}
