package nextstep.security.authentication.convertor;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Map;

public class UsernamePasswordParameterConvertor implements AuthenticationConvertor{
    @Override
    public Authentication convert(final HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];
        return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    }
}
