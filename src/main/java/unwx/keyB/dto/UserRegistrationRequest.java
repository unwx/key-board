package unwx.keyB.dto;

public class UserRegistrationRequest extends UserLoginRequest {

    private final String email;

    public UserRegistrationRequest(String username, String password, String email) {
        super(username, password);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
