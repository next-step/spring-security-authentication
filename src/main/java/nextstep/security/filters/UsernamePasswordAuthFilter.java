package nextstep.security.filters;


import nextstep.security.constants.SecurityConstants;
import nextstep.security.credential.UsernamePasswordAuthenticationToken;
import nextstep.security.model.SecurityAuthentication;
import nextstep.security.provider.AuthenticationManager;
import nextstep.security.provider.ProviderManager;
import nextstep.security.provider.UsernameProvider;
import nextstep.security.service.UserDetailService;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UsernamePasswordAuthFilter extends GenericFilterBean {
    private final AuthenticationManager authenticationManager;

    public UsernamePasswordAuthFilter(UserDetailService userDetailsService) {
        this.authenticationManager = new ProviderManager(
                List.of(new UsernameProvider(userDetailsService))
        );
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!SecurityConstants.LOGIN_URL.equals(((HttpServletRequest) request).getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        try {
            SecurityAuthentication authentication = convert(request);
            if (authentication == null) {
                chain.doFilter(request, response);
                return;
            }

            SecurityAuthentication authenticate = this.authenticationManager.authenticate(authentication);
            HttpSession session = ((HttpServletRequest) request).getSession();
            session.setAttribute(SecurityConstants.SPRING_SECURITY_CONTEXT_KEY, authenticate);
        } catch (
                Exception e) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private SecurityAuthentication convert(ServletRequest request) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            String username = parameterMap.get("username")[0];
            String password = parameterMap.get("password")[0];
            return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        } catch (Exception e) {
            return null;
        }
    }
}
