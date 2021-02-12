package unwx.keyB.dto;

public class AuthenticationResponseDto {

    private String username;
    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AuthenticationResponseDto(String username, String token) {
        this.username = username;
        this.token = token;
    }
}