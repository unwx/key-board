package unwx.keyB.validators;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import unwx.keyB.domains.Article;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ArticleValidatorTest {

    /**
     * article.title.maxlength = 30
     * article.title.minlength = 5
     * article.text.maxlength = 5000
     * article.text.minlength = 15
     */
    @Autowired
    ArticleValidator articleValidator;

    @Test
    public void isOK() {
        assertThat(articleValidator != null).isTrue();
    }

    @Test
    public void nullValidate() {
        Article article = null;
        assertThat(articleValidator.isValid(article)).isFalse();
    }

    @Test
    public void nullAttributes() {
        Article article = new Article();
        assertThat(articleValidator.isValid(article)).isFalse();
    }

    @Test
    public void InvalidTitleMin() {
        Article article = new Article(null, null, "this is correct text...", null);
        article.setTitle("");
        assertThat(articleValidator.isValid(article)).isFalse();
        article.setTitle("1234");
        assertThat(articleValidator.isValid(article)).isFalse();
    }

    @Test
    public void InvalidTitleMax() {
        Article article = new Article(null, null, "this is correct text...", null);
        article.setTitle("1234567890123456789012345678901");
        assertThat(articleValidator.isValid(article)).isFalse();
    }

    @Test
    public void InvalidTextMin() {
        Article article = new Article("this is correct", null, null, null);
        article.setText("qwerty");
        assertThat(articleValidator.isValid(article)).isFalse();
    }

    @Test
    public void InvalidTextMax() {
        Article article = new Article("this is correct", null, null, null);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5100; i++) {
            sb.append(i);
        }
        article.setText(sb.toString());
        assertThat(articleValidator.isValid(article)).isFalse();
    }

    @Test
    public void ValidTest() {
        Article article = new Article(null, null, null, null);
        article.setTitle("correct title");
        article.setText("london is the capital of great britain. ```here's my code```");
        assertThat(articleValidator.isValid(article)).isTrue();
    }
}
