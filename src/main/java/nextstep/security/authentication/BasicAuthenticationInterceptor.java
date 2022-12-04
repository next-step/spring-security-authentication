package nextstep.security.authentication;

import nextstep.security.support.BasicAuthenticationDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public
class BasicAuthenticationInterceptor implements HandlerInterceptor {
    private final BasicAuthenticationDecoder basicAuthenticationDecoder;
    private final AuthenticationProvider authenticationProvider;

    public BasicAuthenticationInterceptor(BasicAuthenticationDecoder basicAuthenticationDecoder, AuthenticationProvider authenticationProvider) {
        this.basicAuthenticationDecoder = basicAuthenticationDecoder;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        authenticationProvider.clearAuthentication();

        BasicAuthenticationToken basicAuthentication = basicAuthenticationDecoder.decode(request);
        String principal = basicAuthentication.getPrincipal().toString();
        String credentials = basicAuthentication.getCredentials().toString();

        authenticationProvider.doAuthentication(new BasicAuthenticationToken(principal, credentials));

        return true;
    }
}
