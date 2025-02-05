package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationErrorHandler;
import nextstep.security.authentication.UserRoleVerifier;
import nextstep.security.authentication.exception.MemberAccessDeniedException;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.user.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class UserRoleFilter extends GenericFilterBean {

    private final UserRoleVerifier userRoleVerifier = new UserRoleVerifier();
    private final SecurityContextRepository securityContextRepository;

    private static final String[] BASIC_AUTH_PATH = new String[]{"/members"};

    public UserRoleFilter(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = Objects.requireNonNull(securityContextRepository);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest request
                && (shouldFilter(request))
        ) {
            SecurityContext context = securityContextRepository.loadContext(request);
            Authentication authentication = context != null
                    ? context.getAuthentication()
                    : SecurityContextHolder.getContext().getAuthentication();

            try {
                if (authentication == null) {
                    throw new MemberAccessDeniedException();
                } else if (authentication instanceof UsernamePasswordAuthenticationToken authenticationToken) {
                    userRoleVerifier.verify(authenticationToken);
                }
            } catch (RuntimeException e) {
                AuthenticationErrorHandler.handleError((HttpServletResponse) servletResponse, e);
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean shouldFilter(HttpServletRequest request) {
        boolean isGetMethod = HttpMethod.GET.name().equalsIgnoreCase(request.getMethod());
        boolean shouldFilterURI = Arrays.stream(BASIC_AUTH_PATH).anyMatch(it -> it.equalsIgnoreCase(request.getRequestURI()));
        return isGetMethod && shouldFilterURI;
    }
}
