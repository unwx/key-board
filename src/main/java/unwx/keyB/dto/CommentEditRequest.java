package unwx.keyB.dto;

public class CommentEditRequest extends CommentRequest {

    private final long commentId;

    public CommentEditRequest(String text,
                              long commentId) {
        super(text);
        this.commentId = commentId;
    }

    public long getCommentId() {
        return commentId;
    }
}
