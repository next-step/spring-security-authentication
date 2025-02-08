package nextstep.security.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class HttpSessionSecurityContextRepository implements SecurityContextRepository {
    private static final String DEFAULT_SESSION_KEY = "SPRING_SECURITY_CONTEXT";

    private final String sessionKey;

    public HttpSessionSecurityContextRepository() {
        this(DEFAULT_SESSION_KEY);
    }

    public HttpSessionSecurityContextRepository(String sessionKey) {
        if (sessionKey == null) {
            sessionKey = DEFAULT_SESSION_KEY;
        }
        this.sessionKey = sessionKey;
    }

    @Override
    public SecurityContext loadContext(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return SecurityContextHolder.createEmptyContext();
        }
        return (SecurityContext) session.getAttribute(sessionKey);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.setAttribute(sessionKey, context);

        SecurityContextHolder.setContext(context);
    }
}
