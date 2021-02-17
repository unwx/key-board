package unwx.keyB.validators;

import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import unwx.keyB.dto.UserLoginRequest;
import unwx.keyB.dto.UserRegistrationRequest;

@Component
@PropertySource("classpath:valid.properties")
public class UserValidator extends Validator{

    @Value("${user.username.minlength}")
    private Integer USERNAME_MIN;

    @Value("${user.username.maxlength}")
    private Integer USERNAME_MAX;

    @Value("${user.password.minlength}")
    private Integer PASSWORD_MIN;

    @Value("${user.password.maxlength}")
    private Integer PASSWORD_MAX;

    @Value("${user.email.minlength}")
    private Integer EMAIL_MIN;

    @Value("${user.email.maxlength}")
    private Integer EMAIL_MAX;

    public boolean isValidLogin(@Nullable UserLoginRequest user) {
        if (user == null)
            return false;

        return areAttributesAreNotNull(user.getPassword(), user.getUsername()) &&
                isValidLengthLogin(user.getUsername().trim().length(), user.getPassword().length());
    }

    public boolean isValidRegistration(@Nullable UserRegistrationRequest user) {
        if (user == null)
            return false;

        EmailValidator validator = EmailValidator.getInstance();

        return areAttributesAreNotNull(user.getPassword(), user.getUsername(), user.getEmail()) &&
                isValidLengthRegistration(user.getUsername().trim().length(), user.getPassword().length(),
                        user.getEmail().trim().length(), user.getEmail(), validator);
    }

    public boolean isFilePicture(MultipartFile file) {
        String type = file.getContentType();
        if (type == null)
            return false;
        return type.endsWith("png") || type.endsWith("jpg") || type.endsWith("jpeg");
    }

    private boolean isValidLengthLogin(int usernameLength, int passwordLength) {
        return usernameLength >= USERNAME_MIN && usernameLength <= USERNAME_MAX
                && passwordLength >= PASSWORD_MIN && passwordLength <= PASSWORD_MAX;
    }

    private boolean isValidLengthRegistration(int usernameLength,
                                              int passwordLength,
                                              int emailLength,
                                              String email,
                                              EmailValidator validator) {
        return usernameLength >= USERNAME_MIN && usernameLength <= USERNAME_MAX
                && passwordLength >= PASSWORD_MIN && passwordLength <= PASSWORD_MAX
                && emailLength >= EMAIL_MIN && emailLength <= EMAIL_MAX && validator.isValid(email);
    }

}
