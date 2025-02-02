package nextstep.security.authentication.converter.form;

import nextstep.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Map;

public class FormAuthentication extends UsernamePasswordAuthenticationToken {
    public FormAuthentication(String username, String password) {
        super(username, password);
    }

    public static FormAuthentication of(Map<String, String[]> parameterMap) {
        return new FormAuthentication(
                username(parameterMap),
                password(parameterMap)
        );
    }

    public static boolean supports(Map<String, String[]> parameterMap) {
        return notBlank(username(parameterMap))
                && notBlank(password(parameterMap));
    }

    private static String username(Map<String, String[]> parameterMap) {
        return parameterMap.get("username")[0];
    }

    private static String password(Map<String, String[]> parameterMap) {
        return parameterMap.get("password")[0];
    }

    private static boolean notBlank(String string) {
        return string != null && !string.isBlank();
    }
}
