package nextstep.security.authentication.filter;

import static nextstep.security.util.SecurityConstants.LOGIN_REQUEST_URI;
import static nextstep.security.util.SecurityConstants.SPRING_SECURITY_CONTEXT_KEY;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.userdetail.UserDetail;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.GenericFilterBean;

public class FormLoginAuthFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;

    public FormLoginAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        try {
            var request = (HttpServletRequest) servletRequest;

            if (!Objects.equals(request.getRequestURI(), LOGIN_REQUEST_URI)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            validateParamAndSession(request);

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unauthenticated(
                    username, password);

            Authentication authenticate = authenticationManager.authenticate(authentication);

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            var response = (HttpServletResponse) servletResponse;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void validateParamAndSession(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (session.getAttribute(SPRING_SECURITY_CONTEXT_KEY) != null) {
            session.removeAttribute(SPRING_SECURITY_CONTEXT_KEY);
        }

        if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(password)) {
            throw new AuthenticationException();
        }
    }

    private void verifyUserDetail(UserDetail userDetail, String password) {
        if (Objects.isNull(userDetail)) {
            throw new AuthenticationException();
        }

        if (!userDetail.verifyPassword(password)) {
            throw new AuthenticationException();
        }
    }
}
