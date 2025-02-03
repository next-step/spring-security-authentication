package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.SecurityContextHolder;
import nextstep.app.util.Base64Convertor;
import nextstep.security.SecurityContext;
import nextstep.security.SecurityContextRepository;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.exception.AuthenticationException;
import nextstep.security.authentication.exception.MemberAccessDeniedException;
import nextstep.security.user.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class BasicAuthFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String NORMAL_USER = "NORMAL_USER";

    public BasicAuthFilter(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !HttpMethod.GET.name().equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {
        try {
            SecurityContext context = getSecurityContext(request);
            if (context.getAuthentication() instanceof UsernamePasswordAuthenticationToken authentication) {
                roleCheckByAuthentication(authentication);
            }
        } catch (RuntimeException e) {
            handleError(response, e);
        }

        filterChain.doFilter(request, response);

    }

    private SecurityContext getSecurityContext(HttpServletRequest request) {
        SecurityContext context = securityContextRepository.loadContext(request);
        if (context != null) {
            return context;
        }

        Authentication authenticationToken = authenticationManager.authenticate(authenticationByRequest(request));
        if (!authenticationToken.isAuthenticated()) {
            throw new AuthenticationException();
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return SecurityContextHolder.getContext();
    }

    private Authentication authenticationByRequest(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(authorization)) {
            throw new AuthenticationException();
        }

        String credentials = authorization.split(" ")[1];
        String decodedString = Base64Convertor.decode(credentials);
        String[] usernameAndPassword = decodedString.split(":");
        if (usernameAndPassword.length != 2) {
            throw new AuthenticationException();
        }

        return UsernamePasswordAuthenticationToken.unAuthorizedToken(usernameAndPassword[0], usernameAndPassword[1]);
    }

    private void roleCheckByAuthentication(UsernamePasswordAuthenticationToken authenticationToken) {
        boolean hasNoRoles = authenticationToken.getAuthorities() == null || authenticationToken.getAuthorities().isEmpty();
        if (hasNoRoles) {
            throw new MemberAccessDeniedException();
        }

        boolean isNormalUser = authenticationToken.getAuthorities().stream()
                .map(Object::toString)
                .anyMatch(NORMAL_USER::equalsIgnoreCase);
        if (isNormalUser) {
            throw new MemberAccessDeniedException();
        }
    }

    private void handleError(HttpServletResponse response, RuntimeException e) throws IOException {
        if (e instanceof MemberAccessDeniedException) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (e instanceof AuthenticationException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
