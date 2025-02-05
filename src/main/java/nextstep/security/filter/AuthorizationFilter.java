package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.Role;
import nextstep.security.core.context.SecurityContextHolder;
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
        final boolean checkedAuthorization = checkAuthorization(request, authentication);

        if (checkedAuthorization) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean checkAuthorization(final HttpServletRequest request, final Authentication authentication) {
        final String requestURI = request.getRequestURI();
        if (restrictedRoutes.containsKey(requestURI)) {
            return blockedRoleCheck(authentication, requestURI);
        }

        return false;
    }

    private boolean blockedRoleCheck(final Authentication authentication, final String requestUri) {
        List<Role> blockedRoles = restrictedRoutes.get(requestUri);

        return authentication.getAuthorities().stream()
                .anyMatch(blockedRoles::contains);
    }
}
