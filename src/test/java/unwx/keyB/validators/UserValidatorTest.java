package unwx.keyB.validators;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import unwx.keyB.dto.UserLoginRequest;
import unwx.keyB.dto.UserRegistrationRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserValidatorTest {

    /**
     * user.username.minlength = 2 <br>
     * user.username.maxlength = 15 <br>
     * user.password.minlength = 8 <br>
     * user.password.maxlength = 30 <br>
     * user.email.minlength = 5 <br>
     * user.email.maxlength = 64 <br>
     */
    @Autowired
    UserValidator userValidator;

    @Test
    public void isOK() {
        assertThat(userValidator != null).isTrue();
    }

    @Test
    public void nullValidate() {
        UserLoginRequest userLoginRequest = null;
        UserRegistrationRequest userRegistrationRequest = null;
        assertThat(userValidator.isValidLogin(userLoginRequest)).isFalse();
        assertThat(userValidator.isValidLogin(userRegistrationRequest)).isFalse();
    }

    @Test
    public void nullAttributesLogin() {
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        assertThat(userValidator.isValidLogin(userLoginRequest)).isFalse();
    }

    @Test
    public void nullAttributesRegistration() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        assertThat(userValidator.isValidRegistration(userRegistrationRequest)).isFalse();
    }

    @Test
    public void InvalidUsernameMinLogin() {
        UserLoginRequest userLoginRequest = new UserLoginRequest("a", "correct pass");
        assertThat(userValidator.isValidLogin(userLoginRequest)).isFalse();
    }

    @Test
    public void InvalidUsernameMinRegistration() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest("a", "correct pass", "email@gmail.com");
        assertThat(userValidator.isValidRegistration(userRegistrationRequest)).isFalse();
    }

    @Test
    public void InvalidUsernameMaxLogin() {
        UserLoginRequest userLoginRequest = new UserLoginRequest("1234567890123456", "correct pass");
        assertThat(userValidator.isValidLogin(userLoginRequest)).isFalse();
    }

    @Test
    public void InvalidUsernameMaxRegistration() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest("1234567890123456", "correct pass", "email@gmail.com");
        assertThat(userValidator.isValidRegistration(userRegistrationRequest)).isFalse();
    }

    @Test
    public void InvalidPasswordMinLogin() {
        UserLoginRequest userLoginRequest = new UserLoginRequest("correct name", "1234567");
        assertThat(userValidator.isValidLogin(userLoginRequest)).isFalse();
    }

    @Test
    public void InvalidPasswordMinRegistration() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest("correct name", "1234567", "email@gmail.com");
        assertThat(userValidator.isValidRegistration(userRegistrationRequest)).isFalse();
    }

    @Test
    public void InvalidPasswordMaxLogin() {
        UserLoginRequest userLoginRequest = new UserLoginRequest("correct name", "1234567890123456789012345678901");
        assertThat(userValidator.isValidLogin(userLoginRequest)).isFalse();
    }

    @Test
    public void InvalidPasswordMaxRegistration() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest("correct name", "1234567890123456789012345678901", "email@gmail.com");
        assertThat(userValidator.isValidRegistration(userRegistrationRequest)).isFalse();
    }

    @Test
    public void InvalidEmailLength() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest("correct name", "correct pass", "123");
        assertThat(userValidator.isValidRegistration(userRegistrationRequest)).isFalse();
        userRegistrationRequest.setEmail(
                        "1234567890" +
                        "1234567890" +
                        "1234567890" +
                        "1234567890" +
                        "1234567890" +
                        "1234567890" +
                        "12345");
        assertThat(userValidator.isValidRegistration(userRegistrationRequest)).isFalse();
    }

    @Test
    public void InvalidEmailPattern() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest("correct name", "correct pass", "123");
        userRegistrationRequest.setEmail("jdfig");
        assertThat(userValidator.isValidRegistration(userRegistrationRequest)).isFalse();
        userRegistrationRequest.setEmail("email1324.com");
        assertThat(userValidator.isValidRegistration(userRegistrationRequest)).isFalse();
    }

    @Test
    public void ValidEmailPattern() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest("correct name", "correct pass", "123");
        userRegistrationRequest.setEmail("test@yahoo.com");
        assertThat(userValidator.isValidRegistration(userRegistrationRequest)).isTrue();
    }

    @Test
    public void ValidLogin() {
        UserLoginRequest userLoginRequest = new UserLoginRequest("correct name", "correct pass");
        assertThat(userValidator.isValidLogin(userLoginRequest)).isTrue();
    }

    @Test
    public void ValidRegistration() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest("correct name", "correct pass", "test@yahoo.com");
        assertThat(userValidator.isValidRegistration(userRegistrationRequest)).isTrue();
    }
}
