package unwx.keyB.exceptions.rest.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    private static final long serialVersionUID = -26532221143087521L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}