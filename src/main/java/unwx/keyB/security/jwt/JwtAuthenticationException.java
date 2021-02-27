package unwx.keyB.security.jwt;

import java.io.Serial;

public class JwtAuthenticationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3754844645603742933L;

    public JwtAuthenticationException(String explanation) {
        super(explanation);
    }

    public JwtAuthenticationException() {
    }

}
