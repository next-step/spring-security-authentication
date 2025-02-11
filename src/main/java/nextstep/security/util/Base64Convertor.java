package nextstep.security.util;

import java.util.Base64;

public final class Base64Convertor {

    private Base64Convertor() {
        throw new AssertionError();
    }

    public static String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    public static String decode(String value) {
        return new String(Base64.getDecoder().decode(value));
    }

}
