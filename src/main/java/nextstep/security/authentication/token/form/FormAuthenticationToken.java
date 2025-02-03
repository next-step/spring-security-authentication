package nextstep.security.authentication.token.form;

import nextstep.security.authentication.token.UsernamePasswordAuthenticationToken;

import java.util.Map;

public class FormAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public FormAuthenticationToken(String username, String password) {
        super(username, password);
    }

    public static FormAuthenticationToken of(Map<String, String[]> parameterMap) {
        return new FormAuthenticationToken(
                usernameParams(parameterMap)[0],
                passwordParams(parameterMap)[0]
        );
    }

    public static boolean supports(Map<String, String[]> parameterMap) {
        return notBlank(usernameParams(parameterMap))
                && notBlank(passwordParams(parameterMap));
    }

    private static String[] usernameParams(Map<String, String[]> parameterMap) {
        return parameterMap.get("username");
    }

    private static String[] passwordParams(Map<String, String[]> parameterMap) {
        return parameterMap.get("password");
    }

    private static boolean notBlank(String[] params) {
        return params != null
                && params.length > 0
                && !params[0].isBlank();
    }
}
