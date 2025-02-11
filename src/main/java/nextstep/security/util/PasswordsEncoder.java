package nextstep.security.util;

public interface PasswordsEncoder {

    String encode(CharSequence rawPassword);
}
