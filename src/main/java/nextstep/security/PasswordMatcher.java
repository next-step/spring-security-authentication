package nextstep.security;

public interface PasswordMatcher {
    boolean matches(String password1, String password2);
}
