package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.Role;
import nextstep.security.core.context.SecurityContextHolder;
import nextstep.security.exception.ForbiddenException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AuthorizationFilter extends OncePerRequestFilter {
    private final Map<String, List<Role>> restrictedRoutes;

    public AuthorizationFilter(Map<String, List<Role>> restrictedRoutes) {
        this.restrictedRoutes = restrictedRoutes;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isUnauthorized(request, authentication)) {
            throw new ForbiddenException();
        }

        filterChain.doFilter(request, response);
    }

    private boolean isUnauthorized(final HttpServletRequest request, final Authentication authentication) {
        final String requestURI = request.getRequestURI();

        return restrictedRoutes.containsKey(requestURI) && isBlockedRole(authentication, requestURI);

    }

    private boolean isBlockedRole(final Authentication authentication, final String requestUri) {
        List<Role> blockedRoles = restrictedRoutes.getOrDefault(requestUri, List.of());

        return authentication.getAuthorities().stream()
                .anyMatch(blockedRoles::contains);
    }
}
