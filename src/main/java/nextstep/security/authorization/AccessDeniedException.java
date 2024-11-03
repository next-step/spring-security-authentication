package nextstep.security.authorization;

/**
 * Thrown if an Authentication object does not hold a required authority.
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String msg) {super(msg);}
    public AccessDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
