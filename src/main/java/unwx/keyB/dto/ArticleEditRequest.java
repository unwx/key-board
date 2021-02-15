package unwx.keyB.dto;

public class ArticleEditRequest extends ArticleCreateRequest{

    private long targetId;

    public ArticleEditRequest(String text, String title, long targetId) {
        super(text, title);
        this.targetId = targetId;
    }

    public ArticleEditRequest() {
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }
}
