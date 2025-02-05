package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.manager.AuthenticationManager;
import nextstep.security.authentication.manager.ProviderManager;
import nextstep.security.authentication.provider.AuthenticationProvider;
import nextstep.security.authentication.provider.UsernamePasswordAuthenticationProvider;
import nextstep.security.authentication.token.AuthenticationTokenConverter;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.user.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.apache.tomcat.websocket.Constants.UNAUTHORIZED;

public class UsernamePasswordAuthenticationFilter extends OncePerRequestFilter {
    private final String uri;
    private final AuthenticationManager manager;
    private final AuthenticationTokenConverter tokenConverter;
    private final SecurityContextRepository securityContextRepository = HttpSessionSecurityContextRepository.getInstance();

    public UsernamePasswordAuthenticationFilter(
            UserDetailsService userDetailsService,
            AuthenticationTokenConverter tokenConverter,
            String uri
    ) {
        final List<AuthenticationProvider> providers = List.of(
                new UsernamePasswordAuthenticationProvider(userDetailsService)
        );
        this.manager = new ProviderManager(providers);
        this.tokenConverter = tokenConverter;
        this.uri = uri;
    }

    public UsernamePasswordAuthenticationFilter(
            UserDetailsService userDetailsService,
            AuthenticationTokenConverter tokenConverter
    ) {
        this(userDetailsService, tokenConverter, null);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        try {
            setContext(request, response);
            chain.doFilter(request, response);
        } catch (AuthenticationException e) {
            response.setStatus(UNAUTHORIZED);
        }
    }

    private void setContext(HttpServletRequest request, HttpServletResponse response) {
        if (uri != null && !uri.equals(request.getRequestURI())) {
            return;
        }
        final SecurityContext context = createSecurityContext(request);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }

    private SecurityContext createSecurityContext(HttpServletRequest request) {
        if (!tokenConverter.supports(request)) {
            return SecurityContext.empty();
        }
        final Authentication authenticationToken = tokenConverter.convert(request);
        return new SecurityContext(manager.authenticate(authenticationToken));
    }
}
