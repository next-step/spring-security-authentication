package nextstep.security.filters;

import nextstep.app.ui.AuthenticationException;
import nextstep.security.constants.SecurityConstants;
import nextstep.security.credential.UsernamePasswordAuthenticationToken;
import nextstep.security.model.SecurityAuthentication;
import nextstep.security.provider.AuthenticationManager;
import nextstep.security.provider.ProviderManager;
import nextstep.security.provider.UsernameProvider;
import nextstep.security.service.UserDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class BasicAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;

    public BasicAuthenticationFilter(UserDetailService userDetailsService) {
        this.authenticationManager = new ProviderManager(
                List.of(new UsernameProvider(userDetailsService))
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            SecurityAuthentication authentication = convert(request);
            if (authentication == null) {
                filterChain.doFilter(request, response);
                return;
            }
            this.authenticationManager.authenticate(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private SecurityAuthentication convert(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return null;
        }

        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, SecurityConstants.AUTHENTICATION_SCHEME_BASIC)) {
            return null;
        }
        if (header.equalsIgnoreCase(SecurityConstants.AUTHENTICATION_SCHEME_BASIC)) {
            throw new AuthenticationException();
        }

        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded = decode(base64Token);
        String token = new String(decoded, StandardCharsets.UTF_8);
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new AuthenticationException();
        }

        return UsernamePasswordAuthenticationToken
                .unauthenticated(token.substring(0, delim), token.substring(delim + 1));
    }

    private byte[] decode(byte[] base64Token) {
        try {
            return Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException ex) {
            throw new AuthenticationException();
        }
    }
}
