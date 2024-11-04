package nextstep.app.ui;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("인증에 실패하셨습니다.");
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
