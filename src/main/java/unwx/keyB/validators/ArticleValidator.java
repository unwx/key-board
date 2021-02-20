package unwx.keyB.validators;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import unwx.keyB.dto.ArticleCreateRequest;
import unwx.keyB.dto.ArticleEditRequest;

@Component
@PropertySource("classpath:valid.properties")
public class ArticleValidator extends Validator{

    @Value("${article.text.maxlength}")
    private Short MAX_TEXT_LENGTH;

    @Value("${article.text.minlength}")
    private Short MIN_TEXT_LENGTH;

    @Value("${article.title.maxlength}")
    private Short MAX_TITLE_LENGTH;

    @Value("${article.title.minlength}")
    private Short MIN_TITLE_LENGTH;

    public boolean isValidToCreate(@Nullable ArticleCreateRequest t) {
        if (t == null)
            return false;

        return areAttributesAreNotNull(
                t.getText(),
                t.getTitle()) &&

                isLengthValid(
                        t.getText().trim().length(),
                        t.getTitle().trim().length());
    }

    public boolean isValidToEdit(@Nullable ArticleEditRequest t) {
        if (t == null)
            return false;

        return areAttributesAreNotNull(
                t.getTargetId(),
                t.getText(),
                t.getTitle()) &&

                isLengthValid(
                        t.getText().trim().length(),
                        t.getTitle().trim().length()) &&

                t.getTargetId() > -1;
    }

    private boolean isLengthValid(int textLength, int titleLength) {
        return textLength >= MIN_TEXT_LENGTH &&
                textLength <= MAX_TEXT_LENGTH &&
                titleLength >= MIN_TITLE_LENGTH &&
                titleLength <= MAX_TITLE_LENGTH;
    }

}
