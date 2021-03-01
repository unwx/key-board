package unwx.keyB.exceptions.internal.dao;

import java.io.Serial;

public class SqlCastException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3568306635039675893L;

    public SqlCastException() {
    }

    public SqlCastException(String message) {
        super(message);
    }

    public SqlCastException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlCastException(Throwable cause) {
        super(cause);
    }
}
