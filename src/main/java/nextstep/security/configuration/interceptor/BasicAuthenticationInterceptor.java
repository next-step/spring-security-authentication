package nextstep.security.configuration.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import nextstep.security.model.UserDetails;
import nextstep.security.service.BasicAuthenticationService;
import nextstep.security.service.UserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import static nextstep.security.utils.Constants.BASIC_TOKEN_PREFIX;
import static nextstep.security.utils.Constants.SPRING_SECURITY_CONTEXT_KEY;

@RequiredArgsConstructor
public class BasicAuthenticationInterceptor implements HandlerInterceptor {

    private final BasicAuthenticationService basicAuthenticationService = new BasicAuthenticationService();
    private final UserDetailsService userDetailsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith(BASIC_TOKEN_PREFIX)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        UserDetails decodedUserDetails = basicAuthenticationService.mapTokenToUserDetails(authorizationHeader);

        if (decodedUserDetails == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, decodedUserDetails);
        return userDetailsService.loadUserByUsername(decodedUserDetails.getUserName()).isPresent();
    }

}
