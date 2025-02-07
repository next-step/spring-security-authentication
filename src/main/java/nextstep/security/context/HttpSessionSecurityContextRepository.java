package nextstep.security.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpSessionSecurityContextRepository implements SecurityContextRepository {

    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";


    @Override
    public SecurityContext loadContext(final HttpServletRequest request) {
        var session = request.getSession();
        var sessionValue = session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        SecurityContext context = SecurityContextHolder.getContext();
        //로그인 필터 탄 후 세션이 없는 경우 세션을 설정함
        if (sessionValue != null) {
            return context;
        }
        //로그인 후 요청인 경우 세션 저장
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
        return context;
    }

    @Override
    public void saveContext(final SecurityContext context, final HttpServletRequest request, final HttpServletResponse response) {
        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }

}
