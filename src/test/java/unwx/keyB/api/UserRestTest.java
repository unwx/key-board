package unwx.keyB.api;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import unwx.keyB.befores.Login;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UserRestTest {

    private static final String url = "/api/user/";
    private final String accessToken = Login.login().getAccessToken();

    public UserRestTest() throws JSONException {
    }

    @Test
    public void ChangeAvatar_NoFile() {
        int code = given()
                .contentType("multipart/form-data")
                .header("Authorization", "Bearer_" + accessToken)
                .when().post(url + "change/avatar").getStatusCode();

        assertThat(code == 500).isTrue();
    }

    @Test
    public void ChangeAvatar_InvalidFile() {
        int code = given()
                .contentType("multipart/form-data")
                .header("Authorization", "Bearer_" + accessToken)
                .body(new File("C:\\Users\\Mrpan\\AppData\\Local\\Postman\\Postman.exe"))
                .when().post(url + "change/avatar").getStatusCode();

        assertThat(code == 500).isTrue();
    }

    @Test
    public void ChangeAvatar_Success() {
        String avatarName = given()
                .contentType("multipart/form-data")
                .header("Authorization", "Bearer_" + accessToken)
                .body(new File("D:\\JavaAppsResources\\keyb\\files\\uploads\\user\\default-user-picture.png"))
                .when().post(url + "change/avatar").toString();

        assertThat(avatarName != null).isTrue();
        assertThat(!avatarName.isEmpty()).isTrue();
        assertThat(avatarName.length() > 16).isTrue();
    }

    @Test
    public void GetAvatar_InvalidName() {
        int code = given()
                .header("Authorization", "Bearer_" + accessToken)
                .when().get(url + "avatar/test").getStatusCode();

        assertThat(code == 400).isTrue();
    }

    @Test
    public void GetAvatar_FileNotExist() {
        int code = given()
                .header("Authorization", "Bearer_" + accessToken)
                .when().get(url + "avatar/default-userQ-picture.png").getStatusCode();

        assertThat(code == 400).isTrue();
    }

    @Test
    public void GetAvatar_Success() {
        int code = given()
                .header("Authorization", "Bearer_" + accessToken)
                .when().get(url + "avatar/default-user-picture.png").getStatusCode();

        assertThat(code == 200).isTrue();
    }
}
