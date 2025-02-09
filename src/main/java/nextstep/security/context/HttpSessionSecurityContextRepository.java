package nextstep.security.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpSessionSecurityContextRepository implements SecurityContextRepository {

    private static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    @Override
    public SecurityContext loadContext(HttpServletRequest request) {
        Object context = request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        if (context == null) {
            return SecurityContextHolder.createEmptyContext();
        }

        return (SecurityContext) context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }
}
