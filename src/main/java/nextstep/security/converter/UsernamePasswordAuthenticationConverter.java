package nextstep.security.converter;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.Authentication;
import nextstep.security.AuthenticationConverter;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Map;

public class UsernamePasswordAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String authRequestUsername = (username != null) ? username : "";
        String authRequestPassword = (password != null) ? password : "";

        return new UsernamePasswordAuthenticationToken(authRequestUsername, authRequestPassword);
    }
}
