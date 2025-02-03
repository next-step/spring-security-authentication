package nextstep.app.util;

import java.util.Base64;

public class Base64Convertor {

    protected Base64Convertor() {
        throw new UnsupportedOperationException();
    }

    public static String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    public static String decode(String value) {
        return new String(Base64.getDecoder().decode(value));
    }
}
