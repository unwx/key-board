package unwx.keyB.dto;

public class UserChangeAvatarDto {
    private String avatarName;

    public UserChangeAvatarDto(String avatarName) {
        this.avatarName = avatarName;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }
}
