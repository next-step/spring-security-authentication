package nextstep.security.util;

public class PlainTextPasswordMatcher implements PasswordMatcher {

    @Override
    public boolean matches(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }
        return rawPassword.equals(storedPassword);
    }
}
