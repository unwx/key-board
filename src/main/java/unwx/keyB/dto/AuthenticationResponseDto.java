package unwx.keyB.dto;

import unwx.keyB.domains.User;

public class AuthenticationResponseDto {

    private User user;
    private JwtDto tokens;

    public AuthenticationResponseDto(User user, JwtDto tokens) {
        this.user = user;
        this.tokens = tokens;
    }

    public JwtDto getTokens() {
        return tokens;
    }

    public void setTokens(JwtDto tokens) {
        this.tokens = tokens;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
