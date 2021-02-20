package unwx.keyB.validators;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest(null, null);
        ArticleEditRequest articleEditRequest = new ArticleEditRequest(null, null, -1L);
        assertThat(articleValidator.isValidToCreate(articleCreateRequest)).isFalse();
        assertThat(articleValidator.isValidToEdit(articleEditRequest)).isFalse();
    }

    @Test
    public void InvalidTitleMinCreate() {
        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest("this is correct text...", "");
        assertThat(articleValidator.isValidToCreate(articleCreateRequest)).isFalse();
        ArticleCreateRequest articleCreateRequest1 = new ArticleCreateRequest("this is correct text...", "1234");
        assertThat(articleValidator.isValidToCreate(articleCreateRequest1)).isFalse();
    }

    @Test
    public void InvalidTitleMinEdit() {
        ArticleEditRequest articleEditRequest = new ArticleEditRequest("this is correct text...", "", 1L);
        assertThat(articleValidator.isValidToEdit(articleEditRequest)).isFalse();
        ArticleEditRequest articleEditRequest1 = new ArticleEditRequest("this is correct text...", "1234", 1L);
        assertThat(articleValidator.isValidToEdit(articleEditRequest1)).isFalse();
    }

    @Test
    public void InvalidTitleMaxCreate() {
        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest("this is correct text...", "1234567890123456789012345678901");
        assertThat(articleValidator.isValidToCreate(articleCreateRequest)).isFalse();
    }

    @Test
    public void InvalidTitleMaxEdit() {
        ArticleEditRequest articleEditRequest = new ArticleEditRequest("this is correct text...", "1234567890123456789012345678901", 1L);
        assertThat(articleValidator.isValidToEdit(articleEditRequest)).isFalse();
    }

    @Test
    public void InvalidTextMinCreate() {
        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest("qwerty", "this is correct");
        assertThat(articleValidator.isValidToCreate(articleCreateRequest)).isFalse();
    }

    @Test
    public void InvalidTextMinEdit() {
        ArticleEditRequest articleEditRequest = new ArticleEditRequest("qwerty", "this is correct", 1L);
        assertThat(articleValidator.isValidToEdit(articleEditRequest)).isFalse();
    }

    @Test
    public void InvalidTextMaxCreate() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5100; i++) {
            sb.append(i);
        }
        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest(sb.toString(), "this is correct");
        assertThat(articleValidator.isValidToCreate(articleCreateRequest)).isFalse();
    }

    @Test
    public void InvalidTextMaxEdit() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5100; i++) {
            sb.append(i);
        }
        ArticleEditRequest articleEditRequest = new ArticleEditRequest(sb.toString(), "this is correct", 1L);
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
