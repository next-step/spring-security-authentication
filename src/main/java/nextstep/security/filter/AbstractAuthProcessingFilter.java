package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.exception.AuthenticationException;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextImpl;
import nextstep.security.context.SecurityContextRepository;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractAuthProcessingFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final String[] shouldFilteringPaths;
    private final HttpMethod[] shouldFilteringMethods;

    protected AbstractAuthProcessingFilter(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository, String[] shouldFilteringPaths, HttpMethod[] shouldFilteringMethods) {
        this.authenticationManager = Objects.requireNonNull(authenticationManager);
        this.securityContextRepository = Objects.requireNonNull(securityContextRepository);
        this.shouldFilteringPaths = Objects.requireNonNull(shouldFilteringPaths);
        this.shouldFilteringMethods = Objects.requireNonNull(shouldFilteringMethods);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest request
                && (servletResponse instanceof HttpServletResponse response)
        ) {
            boolean shouldNotFiltered = isShouldNotFiltered(request);
            if (shouldNotFiltered) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            try {
                Authentication authenticateRequest = getAuthentication(request);

                Authentication authentication = authenticationManager.authenticate(authenticateRequest);
                if (!authentication.isAuthenticated()) {
                    throw new AuthenticationException();
                }

                registerSecurityOnSession(authentication, request, response);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (AuthenticationException e) {
                ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            }
        }
    }

    private void registerSecurityOnSession(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityContext securityContext = new SecurityContextImpl(authentication);
        securityContextRepository.saveContext(securityContext, request, response);
    }

    public abstract Authentication getAuthentication(HttpServletRequest request);

    private boolean isShouldNotFiltered(HttpServletRequest request) {
        boolean isNotPostMethod = Arrays.stream(shouldFilteringMethods).map(HttpMethod::name).noneMatch(it -> it.equalsIgnoreCase(request.getMethod()));
        boolean isNotMatchedURI = Arrays.stream(shouldFilteringPaths).noneMatch(it -> it.equalsIgnoreCase(request.getRequestURI()));
        return isNotMatchedURI || isNotPostMethod;
    }
}
