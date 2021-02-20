package unwx.keyB.dto;

public class ArticleEditRequest extends ArticleCreateRequest{

    private final long targetId;

    public ArticleEditRequest(String text,
                              String title,
                              long targetId) {
        super(text, title);
        this.targetId = targetId;
    }

    public long getTargetId() {
        return targetId;
    }
}
