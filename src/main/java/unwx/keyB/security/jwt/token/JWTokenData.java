package unwx.keyB.security.jwt.token;

public class JWTokenData {

    private String token;
    private long expirationAtMillis;

    public JWTokenData(String token, long expirationAtMillis) {
        this.token = token;
        this.expirationAtMillis = expirationAtMillis;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpirationAtMillis() {
        return expirationAtMillis;
    }

    public void setExpirationAtMillis(long expirationAtMillis) {
        this.expirationAtMillis = expirationAtMillis;
    }
}
