package nextstep.security.login;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.Authentication;
import nextstep.security.AuthenticationConverter;
import nextstep.security.authentication.UsernamePasswordAuthentication;

import java.util.Map;

public class UsernamePasswordAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];

        return new UsernamePasswordAuthentication(username, password);
    }
}
