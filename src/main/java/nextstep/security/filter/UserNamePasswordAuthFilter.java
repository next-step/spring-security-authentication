package nextstep.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.user.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpMethod;

import java.util.Map;

public class UserNamePasswordAuthFilter extends AbstractAuthProcessingFilter {

    public UserNamePasswordAuthFilter(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        super(authenticationManager, securityContextRepository, new String[]{"/login"}, new HttpMethod[]{HttpMethod.POST});
    }

    public Authentication getAuthentication(HttpServletRequest httpRequest) {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];

        return UsernamePasswordAuthenticationToken.unAuthorizedToken(username, password);
    }
}
