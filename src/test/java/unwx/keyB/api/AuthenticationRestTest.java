package unwx.keyB.api;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import unwx.keyB.domains.User;
import unwx.keyB.dto.JwtDto;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * users {
 *     user {
 *         username:qqqqqqqq
 *         password:qqqqqqqq
 *     }
 * }
 */
public class AuthenticationRestTest {

    private final String url = "http://localhost:8080/api/";

    @Test
    public void Login_InvalidUsernameTest() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "-");
        requestParams.put("password", "qqqqqqqq");
        int code = given()
                .body(requestParams.toString()).contentType("application/json")
        .when().post(url + "auth/login").getStatusCode();

        assertThat(code == 400).isTrue();
    }

    @Test
    public void Login_InvalidPasswordTest() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "qqqqqqqq");
        requestParams.put("password", "-");
        int code = given()
                .body(requestParams.toString()).contentType("application/json")
                .when().post(url + "auth/login").getStatusCode();

        assertThat(code == 400).isTrue();
    }

    @Test
    public void Login_WithoutArguments() {
        JSONObject requestParams = new JSONObject();

        int code = given()
                .body(requestParams.toString()).contentType("application/json")
                .when().post(url + "auth/login").getStatusCode();

        assertThat(code == 400).isTrue();
    }

    @Test
    public void Login_IncorrectPasswordTest() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "qqqqqqqq");
        requestParams.put("password", "qwerty1234");
        int code = given()
                .body(requestParams.toString()).contentType("application/json")
                .when().post(url + "auth/login").getStatusCode();

        assertThat(code == 403).isTrue();
    }

    @Test
    public void Login_IncorrectUsernameTest() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "asdkasdljasl");
        requestParams.put("password", "qwerty1234");
        int code = given()
                .body(requestParams.toString()).contentType("application/json")
                .when().post(url + "auth/login").getStatusCode();

        assertThat(code == 403).isTrue();
    }


    @Test
    public void Login_Success() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "qqqqqqqq");
        requestParams.put("password", "qqqqqqqq");
        User user = given()
                .body(requestParams.toString()).contentType("application/json")
                .when().post(url + "auth/login").then().extract().as(User.class);

        assertThat(user.getUsername().equals("qqqqqqqq")).isTrue();
        assertThat(user.getPassword() == null).isTrue();
        assertThat(user.getEmail() != null && !user.getEmail().isEmpty()).isTrue();
        assertThat(user.getAccessToken() != null && !user.getAccessToken().isEmpty()).isTrue();
        assertThat(user.getRefreshToken() != null && !user.getRefreshToken().isEmpty()).isTrue();
        assertThat(user.getAccessTokenExpiration() == null).isTrue();
    }

    @Test
    public void Refresh_withoutToken() {
        int code = given()
                .when().post(url + "auth/refresh").getStatusCode();

        assertThat(code == 400).isTrue();
    }

    @Test
    public void Refresh_InvalidToken() {
        /* this is not a refresh token */
        int code = given()
                .header("Authentication", "Bearer_eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9." +
                        "eyJzdWIiOiJxcXFxcXFxcSIsImlzcyI6ImtleS1iIiwiZXhwaXJhdGlvbiI6IjE2MTM3" +
                        "NDYwODU0NTEiLCJ0eXBlIjoiYWNjZXNzIn0.Kc5SPua6iFR-Gpnsc7Cp24XpH_CR3BPoB" +
                        "1Ol5heJjuKE0bci29m2gicXUOp6enw_w58CoQlfA3vLG1GTlTYMEMFmHF8uUvrenZHDuK" +
                        "jIIdn298gTIAbcxHhjysHFBqnAMQ92Q1Bwlie3LBlGaPjipcTM9NDcLTth62B1SsYP7Yp" +
                        "jCewOgRWCbhaxps6QmI3JQthkDh2UQ_VUW26Lul8FugaepDV8mPk2ENPrLgV11_3KreOt" +
                        "9odWjoikPXMfpH7cdTHjSBhITWhY9fHii4KasZRCUf85Qy8C_ADtaWkhhKsrZ_N5rp7wR" +
                        "FARcB_WLBZWAndCQkvbSqV_t9mxHkoUMA")
                .when().post(url + "auth/refresh").getStatusCode();

        assertThat(code == 400).isTrue();
    }

    @Test
    public void Refresh_ValidToken_Success() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "qqqqqqqq");
        requestParams.put("password", "qqqqqqqq");
        User user = given()
                .body(requestParams.toString()).contentType("application/json")
                .when().post(url + "auth/login").then().extract().as(User.class);
        assertThat(user != null).isTrue();

        JwtDto jwt = given()
                .header("Authorization", "Bearer_" + user.getRefreshToken())
                .when().post(url + "auth/refresh").then().extract().as(JwtDto.class);

        assertThat(jwt.getRefreshToken() != null && jwt.getAccessToken() != null).isTrue();
        assertThat(!jwt.getRefreshToken().isEmpty() && !jwt.getAccessToken().isEmpty()).isTrue();
    }
}
