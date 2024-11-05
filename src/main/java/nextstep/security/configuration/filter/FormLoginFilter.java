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

import nextstep.security.model.authentication.Authentication;
import nextstep.security.model.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.model.context.SecurityContext;
import nextstep.security.repository.HttpSessionSecurityContextRepository;
import nextstep.security.service.UserDetailsService;
import nextstep.security.service.authentication.AuthenticationManager;
import nextstep.security.service.authentication.DaoAuthenticationProvider;
import nextstep.security.service.authentication.ProviderManager;
import nextstep.security.service.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import static nextstep.security.utils.Constants.LOGIN_ENDPOINT_ADDRESS;
import static nextstep.security.utils.Constants.PASSWORD_ATTRIBUTE_NAME;
import static nextstep.security.utils.Constants.USERNAME_ATTRIBUTE_NAME;

@Component
public class FormLoginFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;
    private final HttpSessionSecurityContextRepository httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();

    public FormLoginFilter(UserDetailsService userDetailsService) {
        authenticationManager = new ProviderManager(
                List.of(new DaoAuthenticationProvider(userDetailsService))
        );
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        if (!LOGIN_ENDPOINT_ADDRESS.equals(httpRequest.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        try {
            Authentication authenticationRequest = convert(httpRequest);

            if (Objects.isNull(authenticationRequest)) {
                ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Authentication authenticated = authenticationManager.authenticate(authenticationRequest);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticated);
            SecurityContextHolder.setContext(context);
            httpSessionSecurityContextRepository.saveContext(context, httpRequest,
                    (HttpServletResponse) servletResponse);
        } catch (Exception e) {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    private Authentication convert(HttpServletRequest httpRequest) {
        try {
            return UsernamePasswordAuthenticationToken.unauthenticated(
                    httpRequest.getParameter(USERNAME_ATTRIBUTE_NAME),
                    httpRequest.getParameter(PASSWORD_ATTRIBUTE_NAME));
        } catch (Exception e) {
            return null;
        }
    }
}
