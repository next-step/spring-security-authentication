package nextstep.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.domain.MemberDetail;
import nextstep.security.domain.MemberDetailService;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.util.Base64Convertor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class BasicAuthenticationFilter extends GenericFilterBean {

    private final MemberDetailService memberDetailService;

    public BasicAuthenticationFilter(MemberDetailService memberDetailService) {
        this.memberDetailService = memberDetailService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (!httpRequest.getRequestURI().startsWith("/members")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String authorization = httpRequest.getHeader("Authorization");
            String credentials = authorization.split(" ")[1];
            String decodedString = Base64Convertor.decode(credentials);
            String[] usernameAndPassword = decodedString.split(":");
            String username = usernameAndPassword[0];
            String password = usernameAndPassword[1];

            MemberDetail member = memberDetailService.findByUsername(username);
            if (!member.isCorrectPassword(password)) {
                throw new AuthenticationException();
            }

        } catch (Exception e) {
            throw new AuthenticationException();
        }

        chain.doFilter(request, response);
    }
}
