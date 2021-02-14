package unwx.keyB.validators;

import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import unwx.keyB.domains.User;

@Component
@PropertySource("classpath:valid.properties")
public class UserValidator {

    @Value("${user.username.minlength}")
    private Integer usernameMin;

    @Value("${user.username.maxlength}")
    private Integer usernameMax;

    @Value("${user.password.minlength}")
    private Integer passwordMin;

    @Value("${user.password.maxlength}")
    private Integer passwordMax;

    @Value("${user.email.minlength}")
    private Integer emailMin;

    @Value("${user.email.maxlength}")
    private Integer emailMax;

    public boolean isValidLogin(@Nullable User user) {
        if (user == null)
            return false;

        if (user.getUsername() == null ||
                user.getPassword() == null)
            return false;

        int usernameLen = user.getUsername().length();
        int passwordLen = user.getPassword().length();

        return usernameLen >= usernameMin && usernameLen <= usernameMax
                && passwordLen >= passwordMin && passwordLen <= passwordMax;
    }

    public boolean isValidRegistration(@Nullable User user) {
        if (user == null)
            return false;

        if (user.getUsername() == null ||
                user.getPassword() == null ||
                user.getEmail() == null)
            return false;

        int usernameLen = user.getUsername().length();
        int passwordLen = user.getPassword().length();
        int emailLen = user.getEmail().length();
        EmailValidator validator = EmailValidator.getInstance();

        return usernameLen >= usernameMin && usernameLen <= usernameMax
                && passwordLen >= passwordMin && passwordLen <= passwordMax
                && emailLen >= emailMin && emailLen <= emailMax && validator.isValid(user.getEmail());
    }

}
