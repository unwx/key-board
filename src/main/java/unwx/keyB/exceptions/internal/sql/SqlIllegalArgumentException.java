package unwx.keyB.exceptions.internal.sql;

import java.io.Serial;

public class SqlIllegalArgumentException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5379091775637160020L;

    public SqlIllegalArgumentException(String message) {
        super(message);
    }

    public SqlIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlIllegalArgumentException(Throwable cause) {
        super(cause);
    }
}
