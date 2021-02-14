package unwx.keyB.validators;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import unwx.keyB.domains.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserValidatorTest {

    /**
     * user.username.minlength = 2
     * user.username.maxlength = 15
     * user.password.minlength = 8
     * user.password.maxlength = 30
     * user.email.minlength = 5
     * user.email.maxlength = 64
     */
    @Autowired
    UserValidator userValidator;

    @Test
    public void isOK() {
        assertThat(userValidator != null).isTrue();
    }

    @Test
    public void nullValidateLogin() {
        User user = null;
        assertThat(userValidator.isValidLogin(user)).isFalse();
    }

    @Test
    public void nullValidateRegistration() {
        User user = null;
        assertThat(userValidator.isValidRegistration(user)).isFalse();
    }

    @Test
    public void nullAttributesLogin() {
        User user = new User();
        assertThat(userValidator.isValidLogin(user)).isFalse();
    }

    @Test
    public void nullAttributesRegistration() {
        User user = new User();
        assertThat(userValidator.isValidRegistration(user)).isFalse();
    }

    @Test
    public void InvalidUsernameMinLogin() {
        User user = new User("a", "correct pass", false, null, null, null, null);
        assertThat(userValidator.isValidLogin(user)).isFalse();
    }

    @Test
    public void InvalidUsernameMinRegistration() {
        User user = new User("a", "correct pass", false, null, null, null, null);
        assertThat(userValidator.isValidRegistration(user)).isFalse();
    }

    @Test
    public void InvalidUsernameMaxLogin() {
        User user = new User("1234567890123456", "correct pass", false, null, null, null, null);
        assertThat(userValidator.isValidLogin(user)).isFalse();
    }

    @Test
    public void InvalidUsernameMaxRegistration() {
        User user = new User("1234567890123456", "correct pass", false, null, null, null, null);
        assertThat(userValidator.isValidRegistration(user)).isFalse();
    }

    @Test
    public void InvalidPasswordMinLogin() {
        User user = new User("correct name", "1234567", false, null, null, null, null);
        assertThat(userValidator.isValidLogin(user)).isFalse();
    }

    @Test
    public void InvalidPasswordMinRegistration() {
        User user = new User("correct name", "1234567", false, null, null, null, null);
        assertThat(userValidator.isValidRegistration(user)).isFalse();
    }

    @Test
    public void InvalidPasswordMaxLogin() {
        User user = new User("correct name", "1234567890123456789012345678901", false, null, null, null, null);
        assertThat(userValidator.isValidLogin(user)).isFalse();
    }

    @Test
    public void InvalidPasswordMaxRegistration() {
        User user = new User("correct name", "1234567890123456789012345678901", false, null, null, null, null);
        assertThat(userValidator.isValidRegistration(user)).isFalse();
    }

    @Test
    public void InvalidEmailLength() {
        User user = new User("correct name", "correct pass", false, null, "1234", null, null);
        assertThat(userValidator.isValidRegistration(user)).isFalse();
        user.setEmail(
                        "1234567890" +
                        "1234567890" +
                        "1234567890" +
                        "1234567890" +
                        "1234567890" +
                        "1234567890" +
                        "12345");
        assertThat(userValidator.isValidRegistration(user)).isFalse();
    }

    @Test
    public void InvalidEmailPattern() {
        User user = new User("correct name", "correct pass", false, null, null, null, null);
        user.setEmail("jdfig");
        assertThat(userValidator.isValidRegistration(user)).isFalse();
        user.setEmail("email1324.com");
        assertThat(userValidator.isValidRegistration(user)).isFalse();
    }

    @Test
    public void ValidEmailPattern() {
        User user = new User("correct name", "correct pass", false, null, null, null, null);
        user.setEmail("test@yahoo.com");
        assertThat(userValidator.isValidRegistration(user)).isTrue();
    }

    @Test
    public void ValidLogin() {
        User user = new User("correct name", "correct pass", false, null, null, null, null);
        assertThat(userValidator.isValidLogin(user)).isTrue();
    }

    @Test
    public void ValidRegistration() {
        User user = new User("correct name", "correct pass", false, null, "test@yahoo.com", null, null);
        assertThat(userValidator.isValidRegistration(user)).isTrue();
    }
}
