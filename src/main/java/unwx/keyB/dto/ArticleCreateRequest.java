package unwx.keyB.dto;

public class ArticleCreateRequest {
    private String text;
    private String title;

    public ArticleCreateRequest(String text, String title) {
        this.text = text;
        this.title = title;
    }

    public ArticleCreateRequest() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
