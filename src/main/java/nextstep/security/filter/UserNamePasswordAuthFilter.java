package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.SecurityContext;
import nextstep.security.SecurityContextImpl;
import nextstep.security.SecurityContextRepository;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.exception.AuthenticationException;
import nextstep.security.user.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class UserNamePasswordAuthFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    private static final String[] USER_NAME_PASSWORD_AUTH_PATH = new String[]{"/login"};

    public UserNamePasswordAuthFilter(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = Objects.requireNonNull(authenticationManager);
        this.securityContextRepository = Objects.requireNonNull(securityContextRepository);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest request) {
            boolean isNotPostMethod = !HttpMethod.POST.name().equalsIgnoreCase(request.getMethod());
            boolean isNotMatchedURI = Arrays.stream(USER_NAME_PASSWORD_AUTH_PATH).noneMatch(it -> it.equalsIgnoreCase(request.getRequestURI()));
            if (isNotMatchedURI || isNotPostMethod) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            try {
                HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
                Authentication authentication = getAuthenticationByUserNamePassword(httpRequest);

                SecurityContext securityContext = new SecurityContextImpl(authentication);
                securityContextRepository.saveContext(securityContext, httpRequest, (HttpServletResponse) servletResponse);

            } catch (AuthenticationException e) {
                ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            }
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
