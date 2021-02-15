package unwx.keyB.validators;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import unwx.keyB.domains.Article;
import unwx.keyB.dto.ArticleCreateRequest;
import unwx.keyB.dto.ArticleEditRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ArticleValidatorTest {

    /**
     * article.title.maxlength = 30 <br>
     * article.title.minlength = 5 <br>
     * article.text.maxlength = 5000 <br>
     * article.text.minlength = 15 <br>
     */
    @Autowired
    ArticleValidator articleValidator;

    @Test
    public void isOK() {
        assertThat(articleValidator != null).isTrue();
    }

    @Test
    public void nullValidate() {
        ArticleCreateRequest articleCreateRequest = null;
        ArticleEditRequest articleEditRequest = null;
        assertThat(articleValidator.isValidToCreate(articleCreateRequest)).isFalse();
        assertThat(articleValidator.isValidToEdit(articleEditRequest)).isFalse();
    }

    @Test
    public void nullAttributes() {
        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest();
        ArticleEditRequest articleEditRequest = new ArticleEditRequest();
        assertThat(articleValidator.isValidToCreate(articleCreateRequest)).isFalse();
        assertThat(articleValidator.isValidToEdit(articleEditRequest)).isFalse();
    }

    @Test
    public void InvalidTitleMinCreate() {
        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest("this is correct text...", null);
        articleCreateRequest.setTitle("");
        assertThat(articleValidator.isValidToCreate(articleCreateRequest)).isFalse();
        articleCreateRequest.setTitle("1234");
        assertThat(articleValidator.isValidToCreate(articleCreateRequest)).isFalse();
    }

    @Test
    public void InvalidTitleMinEdit() {
        ArticleEditRequest articleEditRequest = new ArticleEditRequest("this is correct text...", null, 1L);
        articleEditRequest.setTitle("");
        assertThat(articleValidator.isValidToEdit(articleEditRequest)).isFalse();
        articleEditRequest.setTitle("1234");
        assertThat(articleValidator.isValidToEdit(articleEditRequest)).isFalse();
    }

    @Test
    public void InvalidTitleMaxCreate() {
        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest("this is correct text...", null);
        articleCreateRequest.setTitle("1234567890123456789012345678901");
        assertThat(articleValidator.isValidToCreate(articleCreateRequest)).isFalse();
    }

    @Test
    public void InvalidTitleMaxEdit() {
        ArticleEditRequest articleEditRequest = new ArticleEditRequest("this is correct text...", null, 1L);
        articleEditRequest.setTitle("1234567890123456789012345678901");
        assertThat(articleValidator.isValidToEdit(articleEditRequest)).isFalse();
    }

    @Test
    public void InvalidTextMinCreate() {
        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest(null, "this is correct");
        Article article = new Article("this is correct", null, null, null);
        article.setText("qwerty");
        assertThat(articleValidator.isValidToCreate(articleCreateRequest)).isFalse();
    }

    @Test
    public void InvalidTextMinEdit() {
        ArticleEditRequest articleEditRequest = new ArticleEditRequest(null, "this is correct", 1L);
        articleEditRequest.setText("qwerty");
        assertThat(articleValidator.isValidToEdit(articleEditRequest)).isFalse();
    }

    @Test
    public void InvalidTextMaxCreate() {
        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest(null, "this is correct");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5100; i++) {
            sb.append(i);
        }
        articleCreateRequest.setText(sb.toString());
        assertThat(articleValidator.isValidToCreate(articleCreateRequest)).isFalse();
    }

    @Test
    public void InvalidTextMaxEdit() {
        ArticleEditRequest articleEditRequest = new ArticleEditRequest(null, "this is correct", 1L);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5100; i++) {
            sb.append(i);
        }
        articleEditRequest.setText(sb.toString());
        assertThat(articleValidator.isValidToEdit(articleEditRequest)).isFalse();
    }

    @Test
    public void ValidTestCreate() {
        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest("this is correct123", "correct title");
        assertThat(articleValidator.isValidToCreate(articleCreateRequest)).isTrue();
    }

    @Test
    public void ValidTestEdit() {
        ArticleEditRequest articleEditRequest = new ArticleEditRequest("this is correct123", "this is correct", 1L);
        assertThat(articleValidator.isValidToEdit(articleEditRequest)).isTrue();
    }
}
