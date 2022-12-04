package nextstep.security.authentication;

import nextstep.security.context.SecurityContextHolder;
import nextstep.security.exception.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HttpBasicAuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationProvider provider;

    public HttpBasicAuthenticationInterceptor(AuthenticationProvider provider) {
        this.provider = provider;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            throw new AuthenticationException();
        }

        final String usernameAndPassword = new String(
            Base64.getDecoder().decode(header.split(" ")[1]),
            StandardCharsets.UTF_8
        );
        final String username = usernameAndPassword.split(":")[0];
        final String password = usernameAndPassword.split(":")[1];

        final Authentication authentication = provider.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(
                username,
                password
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
