package nextstep.authentication.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class HttpSessionSecurityContextRepository implements SecurityContextRepository {

    private static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    @Override
    public SecurityContext loadContext(HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return null;
        }

        return (SecurityContext) httpSession.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {

        // SecurityContext가 null이거나 인증정보가 없으면 세션에서 제거
        if (context == null || context.getAuthentication() == null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(SPRING_SECURITY_CONTEXT_KEY);
            }
            return;
        }

        // 세션이 없으면 생성하고, SecurityContext를 저장
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }
}
