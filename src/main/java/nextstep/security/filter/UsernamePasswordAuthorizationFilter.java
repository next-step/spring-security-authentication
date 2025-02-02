package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.converter.AuthenticationConverter;
import nextstep.security.authentication.manager.AuthenticationManager;
import nextstep.security.authentication.manager.ProviderManager;
import nextstep.security.authentication.provider.AuthenticationProvider;
import nextstep.security.authentication.provider.UsernamePasswordAuthenticationProvider;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.user.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class UsernamePasswordAuthorizationFilter extends OncePerRequestFilter {
    private final AuthenticationManager manager;
    private final AuthenticationConverter converter;

    public UsernamePasswordAuthorizationFilter(
            UserDetailsService userDetailsService,
            AuthenticationConverter converter
    ) {
        final List<AuthenticationProvider> providers = List.of(
                new UsernamePasswordAuthenticationProvider(userDetailsService)
        );
        this.manager = new ProviderManager(providers);
        this.converter = converter;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        SecurityContextHolder.setContext(createSecurityContext(request));
        filterChain.doFilter(request, response);
    }

    private SecurityContext createSecurityContext(HttpServletRequest request) {
        if (!converter.supports(request)) {
            return SecurityContext.empty();
        }
        final Authentication authenticationToken = converter.convert(request);
        return new SecurityContext(manager.authenticate(authenticationToken));
    }
}
