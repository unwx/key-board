package unwx.keyB.api;

import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import unwx.keyB.befores.Login;
import unwx.keyB.domains.Article;
import unwx.keyB.dto.ArticleCreateRequest;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ArticleRestTest {

    private static final String url = "/api/article/";
    private final String accessToken = Login.login().getAccessToken();


    public ArticleRestTest() throws JSONException {}

    @Test
    public void GetAllNoToken() {
        int code = given()
                .contentType("application/json")
                .when().get(url).getStatusCode();

        assertThat(code == 403).isTrue();
    }

    @Test
    public void GetAllWithToken() {
        int code = given()
                .contentType("application/json")
                .header("Authorization", "Bearer_" + accessToken)
                .when().get(url).getStatusCode();
        assertThat(code == 200).isTrue();
    }

    @Test
    public void CreateNoToken() {
        int code = given()
                .contentType("application/json")
                .body(new Article())
                .when().post(url + "create").getStatusCode();
        assertThat(code == 403).isTrue();
    }

    @Test
    public void CreateWithToken_ArticleInvalid() {
        int code = given()
                .contentType("application/json")
                .body(new Article())
                .header("Authorization", "Bearer_" + accessToken)
                .when().post(url + "create").getStatusCode();
        assertThat(code == 400).isTrue();
    }

    @Test
    public void CreateWithToken_ArticleInvalid1() {
        int code = given()
                .contentType("application/json")
                .body(new Article("qwe", "asdasd", "ewfdsf", LocalDateTime.now(), null, -5, null))
                .header("Authorization", "Bearer_" + accessToken)
                .when().post(url + "create").getStatusCode();
        assertThat(code == 400).isTrue();
    }

    @Test
    public void CreateWithToken_Valid() {
        String authorUsername = "qqqqqqqq";
        Response response = given()
                .contentType("application/json")
                .body(new ArticleCreateRequest("1234567890123456", "132456"))
                .header("Authorization", "Bearer_" + accessToken)
                .when().post(url);
        assertThat(response.getStatusCode() == 200).isTrue();

        assertThat(response
                .getBody()
                .jsonPath()
                .getObject("", Article.class) != null)
                .isTrue();

        assertThat(response
                .getBody()
                .jsonPath()
                .getObject("", Article.class)
                .getId() > -1)
                .isTrue();

        assertThat(response
                .getBody()
                .jsonPath()
                .getObject("", Article.class)
                .getAuthor()
                .getUsername()
                .equals(authorUsername))
                .isTrue();
    }
}
