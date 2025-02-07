package nextstep.security.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.convertor.AuthenticationConvertor;
import nextstep.security.authentication.convertor.BasicAuthenticationConvertor;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class BasicAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationConvertor authenticationConvertor;

    public BasicAuthenticationFilter(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.authenticationConvertor = new BasicAuthenticationConvertor();
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) request;
        var httpServletResponse = (HttpServletResponse) response;
        try {
            doFilterInternal(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        chain.doFilter(request, response);
    }

    private void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response) {
        var usernamePassword = authenticationConvertor.convert(request);
        if (usernamePassword == null) {
            return;
        }

        var unauthenticatedToken = authenticationConvertor.convert(request);
        var authentication = authenticationManager.authenticate(unauthenticatedToken);
        if (!authentication.isAuthenticated()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
