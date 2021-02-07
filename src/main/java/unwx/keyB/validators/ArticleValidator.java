package unwx.keyB.validators;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import unwx.keyB.domains.Article;

/**
 * '@Valid', BindingResult doesn't work :(
 *  memory safe, non static
 */
@Component
@PropertySource("classpath:valid.properties")
public class ArticleValidator {

    @Value("${article.text.maxlength}")
    private Short MAX_TEXT_LENGTH;

    @Value("${article.text.minlength}")
    private Short MIN_TEXT_LENGTH;

    @Value("${article.title.maxlength}")
    private Short MAX_TITLE_LENGTH;

    @Value("${article.title.minlength}")
    private Short MIN_TITLE_LENGTH;

    public boolean isValid(@Nullable Article t){
        if (t == null)
            return false;

        String text = t.getText().trim();
        String title = t.getTitle().trim();

        return text.length() >= MIN_TEXT_LENGTH &&
                text.length() <= MAX_TEXT_LENGTH &&
                title.length() >= MIN_TITLE_LENGTH &&
                title.length() <= MAX_TITLE_LENGTH;

    }
}
