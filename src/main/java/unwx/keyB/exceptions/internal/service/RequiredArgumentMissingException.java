package unwx.keyB.exceptions.internal.service;

import java.io.Serial;

public class RequiredArgumentMissingException extends Exception {
    @Serial
    private static final long serialVersionUID = -5815117157357653982L;

    public RequiredArgumentMissingException(String message) {
        super(message);
    }

    public RequiredArgumentMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequiredArgumentMissingException(Throwable cause) {
        super(cause);
    }
}
