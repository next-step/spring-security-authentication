package nextstep.security.util;

public interface PasswordMatcher {
    boolean matches(String rawPassword, String storedPassword);
}
