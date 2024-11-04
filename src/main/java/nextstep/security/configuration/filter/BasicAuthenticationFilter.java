package nextstep.security.configuration.filter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.model.UserDetails;
import nextstep.security.service.BasicAuthenticationService;
import nextstep.security.service.UserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

import static nextstep.security.utils.Constants.BASIC_TOKEN_PREFIX;
import static nextstep.security.utils.Constants.GET_MEMBERS_ENDPOINT_ADDRESS;
import static nextstep.security.utils.Constants.SPRING_SECURITY_CONTEXT_KEY;

@RequiredArgsConstructor
public class BasicAuthenticationFilter extends GenericFilterBean {

    private final BasicAuthenticationService basicAuthenticationService = new BasicAuthenticationService();
    private final UserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        if (!GET_MEMBERS_ENDPOINT_ADDRESS.equals(httpRequest.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith(BASIC_TOKEN_PREFIX)) {
                throw new AuthenticationException();
            }

            UserDetails decodedUserDetails = basicAuthenticationService.mapTokenToUserDetails(authorizationHeader);

            if (Objects.isNull(decodedUserDetails)) {
                throw new AuthenticationException();
            }

            Optional<UserDetails> userDetails = userDetailsService.loadUserByUsernameAndEmail(
                    decodedUserDetails.getUserName(),
                    decodedUserDetails.getPassword());

            if (userDetails.isEmpty()) {
                throw new AuthenticationException();
            }

            httpRequest.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetails.get());
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}
