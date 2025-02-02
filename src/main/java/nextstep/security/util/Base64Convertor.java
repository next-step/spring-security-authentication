package nextstep.security.util;

import java.util.Base64;

public class Base64Convertor {
    public static String decode(String value) {
        return new String(Base64.getDecoder().decode(value));
    }
}
