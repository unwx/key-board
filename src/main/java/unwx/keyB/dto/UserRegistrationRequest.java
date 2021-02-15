package unwx.keyB.dto;

public class UserRegistrationRequest extends UserLoginRequest {

    private String email;

    public UserRegistrationRequest(String username, String password, String email) {
        super(username, password);
        this.email = email;
    }

    public UserRegistrationRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
