package nextstep.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static nextstep.security.filter.LoginAuthenticationFilter.SPRING_SECURITY_CONTEXT_KEY;

public class HttpSessionSecurityContextRepository implements SecurityContextRepository {

    @Override
    public SecurityContext loadContext(HttpServletRequest request) {
        if (request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY) == null) {
            return SecurityContextHolder.createEmptyContext();
        }
        return (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }
}
