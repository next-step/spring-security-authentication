package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationConverter;
import nextstep.security.authentication.AuthenticationErrorHandler;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.exception.AuthenticationException;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class SecurityContextFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationConverter converter = new AuthenticationConverter();
    private final SecurityContextRepository securityContextRepository;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String[] BASIC_AUTH_PATH = new String[]{"/members"};

    public SecurityContextFilter(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = Objects.requireNonNull(authenticationManager);
        this.securityContextRepository = Objects.requireNonNull(securityContextRepository);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        boolean isNotGetMethod = !HttpMethod.GET.name().equalsIgnoreCase(request.getMethod());
        boolean shouldNotFilterURI = Arrays.stream(BASIC_AUTH_PATH).noneMatch(it -> it.equalsIgnoreCase(request.getRequestURI()));
        return isNotGetMethod || shouldNotFilterURI;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {

        try {
            SecurityContext context = securityContextRepository.loadContext(request);
            if (context != null) {
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authentication = authenticateByRequestHeader(request);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (AuthenticationException e) {
            AuthenticationErrorHandler.handleError(response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Authentication authenticateByRequestHeader(HttpServletRequest request) {
        Authentication authentication = converter.convert(request.getHeader(AUTHORIZATION_HEADER));

        Authentication result = authenticationManager.authenticate(authentication);
        if (!result.isAuthenticated()) {
            throw new AuthenticationException();
        }

        return result;
    }
}
