package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.SecurityContextImpl;
import nextstep.security.SecurityContext;
import nextstep.security.SecurityContextRepository;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.exception.AuthenticationException;
import nextstep.security.user.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Map;

public class UserNamePasswordAuthFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public UserNamePasswordAuthFilter(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            Authentication authentication = getAuthenticationByUserNamePassword(httpRequest);

            SecurityContext securityContext = new SecurityContextImpl(authentication);
            securityContextRepository.saveContext(securityContext, httpRequest, (HttpServletResponse) servletResponse);

        } catch (AuthenticationException e) {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    private Authentication getAuthenticationByUserNamePassword(HttpServletRequest httpRequest) {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];

        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unAuthorizedToken(username, password);

        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (!authenticate.isAuthenticated()) {
            throw new AuthenticationException();
        }

        return authenticate;
    }
}
