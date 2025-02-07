package nextstep.security.util;

import java.util.Objects;

public class PlainTextPasswordMatcher implements PasswordMatcher {

    @Override
    public boolean matches(String rawPassword, String storedPassword) {
        return Objects.equals(rawPassword, storedPassword);
    }
}
