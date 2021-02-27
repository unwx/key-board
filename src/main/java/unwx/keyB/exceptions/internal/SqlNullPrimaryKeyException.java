package unwx.keyB.exceptions.internal;

import java.io.Serial;

public class SqlNullPrimaryKeyException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 905955604801180650L;

    public SqlNullPrimaryKeyException() {
    }

    public SqlNullPrimaryKeyException(String message) {
        super(message);
    }

    public SqlNullPrimaryKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlNullPrimaryKeyException(Throwable cause) {
        super(cause);
    }
}
