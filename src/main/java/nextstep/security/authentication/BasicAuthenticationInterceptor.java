package nextstep.security.authentication;

import nextstep.security.support.BasicAuthenticationDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public
class BasicAuthenticationInterceptor implements HandlerInterceptor {

    private final UserAuthenticationService userAuthenticationService;

    private final BasicAuthenticationDecoder basicAuthenticationDecoder;

    public BasicAuthenticationInterceptor(UserAuthenticationService userAuthenticationService, BasicAuthenticationDecoder basicAuthenticationDecoder) {
        this.userAuthenticationService = userAuthenticationService;
        this.basicAuthenticationDecoder = basicAuthenticationDecoder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        BasicAuthenticationToken basicAuthentication = basicAuthenticationDecoder.decode(request);

        userAuthenticationService.validateMember(
                basicAuthentication.getPrincipal().toString(),
                basicAuthentication.getCredentials().toString()
        );

        return true;
    }
}
