package nextstep.security.configuration.filter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nextstep.app.ui.AuthenticationException;
import nextstep.security.configuration.authentication.Authentication;
import nextstep.security.configuration.authentication.AuthenticationManager;
import nextstep.security.configuration.authentication.DaoAuthenticationProvider;
import nextstep.security.configuration.authentication.ProviderManager;
import nextstep.security.configuration.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.model.UserDetails;
import nextstep.security.service.BasicAuthenticationService;
import nextstep.security.service.UserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

import static nextstep.security.utils.Constants.BASIC_TOKEN_PREFIX;
import static nextstep.security.utils.Constants.GET_MEMBERS_ENDPOINT_ADDRESS;
import static nextstep.security.utils.Constants.SPRING_SECURITY_CONTEXT_KEY;

public class BasicAuthenticationFilter extends GenericFilterBean {

    private final BasicAuthenticationService basicAuthenticationService = new BasicAuthenticationService();
    private final AuthenticationManager authenticationManager;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        authenticationManager = new ProviderManager(List.of(new DaoAuthenticationProvider(userDetailsService)));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        if (!GET_MEMBERS_ENDPOINT_ADDRESS.equals(httpRequest.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        try {
            Authentication authenticationRequest = convert(httpRequest);
            httpRequest.getSession()
                    .setAttribute(SPRING_SECURITY_CONTEXT_KEY,
                            authenticationManager.authenticate(authenticationRequest));
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    private Authentication convert(HttpServletRequest httpRequest) {
        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith(BASIC_TOKEN_PREFIX)) {
            throw new AuthenticationException();
        }

        UserDetails decodedUserDetails = basicAuthenticationService.mapTokenToUserDetails(authorizationHeader);

        if (Objects.isNull(decodedUserDetails)) {
            throw new AuthenticationException();
        }

        return UsernamePasswordAuthenticationToken.unauthenticated(
                decodedUserDetails.getUserName(),
                decodedUserDetails.getPassword());
    }
}
