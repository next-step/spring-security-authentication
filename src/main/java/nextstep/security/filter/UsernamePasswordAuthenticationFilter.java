package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class UsernamePasswordAuthenticationFilter extends OncePerRequestFilter {
    private static final String DEFAULT_REQUEST_URI = "/login";

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public UsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (alreadyStoredAuthentication()) {
            return;
        }

        if (!DEFAULT_REQUEST_URI.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            String username = parameterMap.get("username")[0];
            String password = parameterMap.get("password")[0];

            final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            storeAuthentication(authenticate, request, response);


        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private void storeAuthentication(Authentication authenticate, HttpServletRequest request, HttpServletResponse response) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticate);

        securityContextRepository.saveContext(securityContext, request, response);
    }

    private boolean alreadyStoredAuthentication() {
        final SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication() != null;
    }
}
