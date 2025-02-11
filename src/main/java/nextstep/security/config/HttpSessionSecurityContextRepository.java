package nextstep.security.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class HttpSessionSecurityContextRepository implements SecurityContextRepository {

    private static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    /**
     * 현 request의 security context를 가지고와서 반환한다.
     * session이 null이면 context 객체는 null을 반환하거나 session에 저장되어 있는 context 객체는 SecurityContext의 인스턴스가 아니다.
     * 새로운 context 객체가 생성되고 리턴된다.
     *
     * @return
     */
    /**
     * HttpServletReqeust의 getSession(boolean create):
     * false: current seesion이 없으면 null 반환 / true: current session 없으면 새로 만든다.
     * <p>
     * 세션이 잘 유지되기 위해서는 getSession을 response가 커밋되기 전에 호출해야한다.
     * cookie를 사용해 세션의 정합성을 보장할때, 새로운 세션을 response가 커밋되고 나서 만든다면 IllegalStateException 발생한다.
     * getSession() == getSession(true)
     */
    @Override
    public SecurityContext loadContext(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // false: current seesion이 없으면 null 반환 / true: current session 없으면 새로 만든다.
        SecurityContext securityContext = readSecurityContextFromSession(session);
        if (securityContext == null) { // 세션에 올바른 security context가 없다면 새로 만든다.
            securityContext = SecurityContextHolder.createEmptyContext();
        }
        return securityContext;
    }

    private SecurityContext readSecurityContextFromSession(HttpSession httpSession) {
        if (httpSession == null) {
            return null;
        }
        Object contextFromSession = httpSession.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        if (contextFromSession == null) {
            return null;
        }
        if (!(contextFromSession instanceof SecurityContext)) {
            return null;
        }
        return (SecurityContext) contextFromSession;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }

}
