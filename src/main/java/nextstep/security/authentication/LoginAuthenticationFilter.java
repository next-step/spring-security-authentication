package nextstep.security.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.convertor.AuthenticationConvertor;
import nextstep.security.authentication.convertor.UsernamePasswordParameterConvertor;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.util.request.HttpRequestMatcher;
import nextstep.security.util.request.RequestMatcher;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class LoginAuthenticationFilter extends GenericFilterBean {
    private final AuthenticationManager authenticationManager;
    private RequestMatcher requestMatcher;
    private AuthenticationConvertor authenticationConvertor;

    public LoginAuthenticationFilter(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.requestMatcher = new HttpRequestMatcher(HttpMethod.POST, "/login");
        this.authenticationConvertor = new UsernamePasswordParameterConvertor();
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) request;
        if (!requestMatcher.matches(httpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }
        var httpServletResponse = (HttpServletResponse) response;

        try {
            var unauthenticatedToken = authenticationConvertor.convert(httpServletRequest);
            var authentication = authenticationManager.authenticate(unauthenticatedToken);
            if (!authentication.isAuthenticated()) {
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            httpServletResponse.setStatus(HttpStatus.OK.value());
        } catch (Exception e) {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    public void setRequestMatcher(final RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    public void setAuthenticationConvertor(final AuthenticationConvertor authenticationConvertor) {
        this.authenticationConvertor = authenticationConvertor;
    }

}
