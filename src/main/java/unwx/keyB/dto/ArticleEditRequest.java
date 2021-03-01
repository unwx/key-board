package unwx.keyB.dto;

public class ArticleEditRequest extends ArticleCreateRequest{

    private final Long targetId;

    public ArticleEditRequest(String text,
                              String title,
                              Long targetId) {
        super(text, title);
        this.targetId = targetId;
    }

    public long getTargetId() {
        return targetId;
    }
}
