package nextstep.app.domain;

import nextstep.security.util.Base64Convertor;
import nextstep.security.util.PasswordsEncoder;

public class Base64PasswordEncoder implements PasswordsEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return Base64Convertor.encode(rawPassword.toString());
    }
}
