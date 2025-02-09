package nextstep.security.authentication.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionSecurityContextRepository implements SecurityContextRepository {

    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private SessionSecurityContextRepository() { }

    public static SessionSecurityContextRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public SecurityContext loadContext(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ?
                null :
                (SecurityContextImpl) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request) {
        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }

    private static final class SingletonHolder {
        private static final SessionSecurityContextRepository INSTANCE = new SessionSecurityContextRepository();
    }

}
