package unwx.keyB.validators;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import unwx.keyB.domains.Article;

/**
 * '@Valid', BindingResult doesn't work :(
 *  memory safe, non static
 */
@Component
public class ArticleValidator {

    /* params without reflection API */
    private final Integer MAX_TEXT_LENGTH = 5000;
    private final Integer MIN_TEXT_LENGTH = 15;
    private final Integer MAX_TITLE_LENGTH = 50;
    private final Integer MIN_TITLE_LENGTH = 5;

    public boolean isValid(@Nullable Article t){
        if (t == null)
            return false;

        String text = t.getText();
        String title = t.getTitle();

        return text.length() >= MIN_TEXT_LENGTH &&
                text.length() <= MAX_TEXT_LENGTH &&
                title.length() >= MIN_TITLE_LENGTH &&
                title.length() <= MAX_TITLE_LENGTH;

    }
}
