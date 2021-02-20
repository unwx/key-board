package unwx.keyB.dto;

public class ArticleCreateRequest {
    private final String text;
    private final String title;

    public ArticleCreateRequest(String text, String title) {
        this.text = text;
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }
}
