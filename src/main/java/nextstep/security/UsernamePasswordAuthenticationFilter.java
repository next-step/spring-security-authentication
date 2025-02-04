package nextstep.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Map;

import static nextstep.security.UsernamePasswordAuthenticationInterceptor.SPRING_SECURITY_CONTEXT_KEY;

public class UsernamePasswordAuthenticationFilter extends GenericFilterBean {

    private final MemberDetailService memberDetailService;

    public UsernamePasswordAuthenticationFilter(MemberDetailService memberDetailService) {
        this.memberDetailService = memberDetailService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (!httpRequest.getContextPath().startsWith("/login")) {
            chain.doFilter(request, response);
            return;
        }

        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];

        MemberDetail member = memberDetailService.findByUsername(username);
        if (!member.isCorrectPassword(password)) {
            throw new AuthenticationException();
        }

        httpRequest.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);

        chain.doFilter(request, response);
    }
}
