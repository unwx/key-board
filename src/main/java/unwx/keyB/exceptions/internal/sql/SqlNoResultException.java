package unwx.keyB.exceptions.internal.sql;

import java.io.Serial;

public class SqlNoResultException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 457220039568515220L;

    public SqlNoResultException(String message) {
        super(message);
    }

    public SqlNoResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlNoResultException(Throwable cause) {
        super(cause);
    }

    public SqlNoResultException() {
    }
}
