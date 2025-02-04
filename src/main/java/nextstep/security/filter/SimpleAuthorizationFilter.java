package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.Authentication;
import nextstep.security.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.exception.AccessDeniedException;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class SimpleAuthorizationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            checkAuthorization();
        } catch (AccessDeniedException e) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }

        chain.doFilter(request, response);
    }

    private void checkAuthorization() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("인증 정보가 없습니다.");
        }

        // for test
        if (authentication.getPrincipal().equals("b@b.com")) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }
}
