package unwx.keyB.security.jwt;

public class JwtAuthenticationException extends RuntimeException {
    private static final long serialVersionUID = -3754844645603742933L;

    public JwtAuthenticationException(String explanation) {
        super(explanation);
    }

    public JwtAuthenticationException() {
    }

}
