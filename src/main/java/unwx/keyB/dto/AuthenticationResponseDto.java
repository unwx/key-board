package unwx.keyB.dto;

import unwx.keyB.domains.User;

public class AuthenticationResponseDto {

    private User user;

    public AuthenticationResponseDto(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
