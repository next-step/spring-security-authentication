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
import javax.servlet.http.HttpSession;

import nextstep.security.configuration.authentication.Authentication;
import nextstep.security.configuration.authentication.AuthenticationManager;
import nextstep.security.configuration.authentication.DaoAuthenticationProvider;
import nextstep.security.configuration.authentication.ProviderManager;
import nextstep.security.configuration.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.service.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import static nextstep.security.utils.Constants.LOGIN_ENDPOINT_ADDRESS;
import static nextstep.security.utils.Constants.PASSWORD_ATTRIBUTE_NAME;
import static nextstep.security.utils.Constants.SPRING_SECURITY_CONTEXT_KEY;
import static nextstep.security.utils.Constants.USERNAME_ATTRIBUTE_NAME;

@Component
public class FormLoginFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;

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

            // 여기서 context holder 확인해서 빠꾸시킬것
            //            if (Objects.nonNull(session.getAttribute(SPRING_SECURITY_CONTEXT_KEY))) {
            //                UserDetails userDetails = (UserDetails) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
            //                return userDetailsService.loadUserByUsernameAndEmail(userDetails.getUserName(), userDetails.getPassword())
            //                        .isPresent();
            //            }

            Authentication authentication = convert(httpRequest);

            if (Objects.isNull(authentication)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            HttpSession session = httpRequest.getSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, authenticationManager.authenticate(authentication));
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
