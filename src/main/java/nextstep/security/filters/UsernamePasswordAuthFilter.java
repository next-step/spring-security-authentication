package nextstep.security.filters;


import lombok.RequiredArgsConstructor;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.constants.SecurityConstants;
import nextstep.security.model.UserDetails;
import nextstep.security.service.UserDetailService;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class UsernamePasswordAuthFilter extends GenericFilterBean {
    private final UserDetailService userDetailService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!SecurityConstants.LOGIN_URL.equals(((HttpServletRequest) request).getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            String username = parameterMap.get("username")[0];
            String password = parameterMap.get("password")[0];
            UserDetails userDetails = userDetailService.getUserDetails(username, password);
            if (!Objects.equals(userDetails.getPassword(), password)) {
                throw new AuthenticationException();
            }
            HttpSession session = ((HttpServletRequest) request).getSession();
            session.setAttribute(SecurityConstants.SPRING_SECURITY_CONTEXT_KEY, userDetails);
        } catch (
                Exception e) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
