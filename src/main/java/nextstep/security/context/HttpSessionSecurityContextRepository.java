package nextstep.security.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class HttpSessionSecurityContextRepository implements SecurityContextRepository {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    public static HttpSessionSecurityContextRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public SecurityContext loadContext(HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        return session == null
                ? null
                : (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }

    private static final class SingletonHolder {
        private static final HttpSessionSecurityContextRepository INSTANCE = new HttpSessionSecurityContextRepository();
    }
}
