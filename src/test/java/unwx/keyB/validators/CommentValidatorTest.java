package unwx.keyB.validators;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import unwx.keyB.dto.CommentCreateRequest;
import unwx.keyB.dto.CommentEditRequest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class CommentValidatorTest {


    private final CommentValidator validator;

    @Autowired
    public CommentValidatorTest(CommentValidator commentValidator) {
        this.validator = commentValidator;
    }

    @Test
    public void Null() {
        CommentCreateRequest commentCreateRequest = null;
        CommentEditRequest commentEditRequest = null;

        assertThat(validator.isValidToCreate(commentCreateRequest)).isFalse();
        assertThat(validator.isValidToEdit(commentEditRequest)).isFalse();
    }

    @Test
    public void NullAttributes() {
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest(null, -1L);
        CommentEditRequest commentEditRequest = new CommentEditRequest(null, -1L);

        assertThat(validator.isValidToCreate(commentCreateRequest)).isFalse();
        assertThat(validator.isValidToEdit(commentEditRequest)).isFalse();
    }

    @Test
    public void invalidTextLength() {
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest("1", 5L);
        CommentEditRequest commentEditRequest = new CommentEditRequest("1", 5L);

        assertThat(validator.isValidToCreate(commentCreateRequest)).isFalse();
        assertThat(validator.isValidToEdit(commentEditRequest)).isFalse();

        StringBuilder sb = new StringBuilder();
        sb.append("s".repeat(2001));
        CommentCreateRequest commentCreateRequest1 = new CommentCreateRequest(sb.toString(), 5L);
        CommentEditRequest commentEditRequest1 = new CommentEditRequest(sb.toString(), 5L);

        assertThat(validator.isValidToCreate(commentCreateRequest1)).isFalse();
        assertThat(validator.isValidToEdit(commentEditRequest1)).isFalse();
    }

    @Test
    public void invalidArticleId_invalidCommentId() {
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest("123", -1L);
        CommentEditRequest commentEditRequest = new CommentEditRequest("123", -123123L);

        assertThat(validator.isValidToCreate(commentCreateRequest)).isFalse();
        assertThat(validator.isValidToEdit(commentEditRequest)).isFalse();
    }

    @Test
    public void validRequest() {
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest("123", 5L);
        CommentEditRequest commentEditRequest = new CommentEditRequest("123", 7L);

        assertThat(validator.isValidToCreate(commentCreateRequest)).isTrue();
        assertThat(validator.isValidToEdit(commentEditRequest)).isTrue();
    }



}
