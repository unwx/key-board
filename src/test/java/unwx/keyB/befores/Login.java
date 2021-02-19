package unwx.keyB.befores;

import org.json.JSONException;
import org.json.JSONObject;
import unwx.keyB.domains.User;

import static io.restassured.RestAssured.given;

public class Login {

    public static User login() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "qqqqqqqq");
        requestParams.put("password", "qqqqqqqq");

        return given()
                .body(requestParams.toString()).contentType("application/json")
                .when().post("http://localhost:8080/api/auth/login").then().extract().as(User.class);
    }
}
