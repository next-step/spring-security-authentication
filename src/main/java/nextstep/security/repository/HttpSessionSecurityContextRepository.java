package nextstep.security.repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nextstep.security.model.context.SecurityContext;
import org.springframework.stereotype.Repository;

import static nextstep.security.utils.Constants.SPRING_SECURITY_CONTEXT_KEY;

/**
This class is not actually running upon JPA, it's kind of mock server
 That's why this class contains logics itself.
 */
@Repository
public class HttpSessionSecurityContextRepository {

    public SecurityContext loadContext(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
    }
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }

}
