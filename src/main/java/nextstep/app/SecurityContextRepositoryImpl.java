package nextstep.app;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.security.SecurityContext;
import nextstep.security.SecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextRepositoryImpl implements SecurityContextRepository {

    private static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    @Override
    public SecurityContext loadContext(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) {
            return null;
        }

        Object attribute = session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);

        if (attribute instanceof SecurityContext securityContext) {
            return securityContext;
        }

        return null;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (session == null) {
            return;
        }

        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }
}
