package unwx.keyB.exceptions.rest.exceptions;

import java.io.Serial;

public class InternalException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8018867196060958178L;

    public InternalException(String message) {
        super(message);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalException(Throwable cause) {
        super(cause);
    }
}
