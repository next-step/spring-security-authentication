package nextstep.security.authentication.token;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

import java.util.Map;

public class FormAuthenticationTokenConverter implements AuthenticationTokenConverter {
    private FormAuthenticationTokenConverter() {}

    public static AuthenticationTokenConverter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        final Map<String, String[]> parameterMap = request.getParameterMap();
        return new UsernamePasswordAuthenticationToken(
                usernameParams(parameterMap)[0],
                passwordParams(parameterMap)[0]
        );
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        final Map<String, String[]> parameterMap = request.getParameterMap();
        return notBlank(usernameParams(parameterMap))
                && notBlank(passwordParams(parameterMap));
    }

    private String[] usernameParams(Map<String, String[]> parameterMap) {
        return parameterMap.get("username");
    }

    private String[] passwordParams(Map<String, String[]> parameterMap) {
        return parameterMap.get("password");
    }

    private boolean notBlank(String[] params) {
        return params != null
                && params.length > 0
                && !params[0].isBlank();
    }

    private static final class SingletonHolder {
        private static final FormAuthenticationTokenConverter INSTANCE = new FormAuthenticationTokenConverter();
    }
}
