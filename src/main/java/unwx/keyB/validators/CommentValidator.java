package unwx.keyB.validators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import unwx.keyB.dto.CommentCreateRequest;
import unwx.keyB.dto.CommentEditRequest;

@Component
@PropertySource("classpath:valid.properties")
public class CommentValidator extends Validator{

    @Value("${comment.text.maxlength}")
    private Short MAX_TEXT_LENGTH;

    @Value("${comment.text.minlength}")
    private Short MIN_TEXT_LENGTH;

    public boolean isValidToCreate(CommentCreateRequest request) {
        if (request == null)
            return false;

        return areAttributesAreNotNull(
                request.getText(),
                request.getArticleId()) &&

                isLengthValid(request.getText().trim().length());
    }

    public boolean isValidToEdit(CommentEditRequest request) {
        if (request == null)
            return false;

        return areAttributesAreNotNull(
                request.getText(),
                request.getCommentId()) &&

                isLengthValid(request.getText().trim().length()) &&

                request.getCommentId() > -1;
    }

    private boolean isLengthValid(int textLength) {
        return textLength >= MIN_TEXT_LENGTH &&
                textLength <= MAX_TEXT_LENGTH;
    }

}
