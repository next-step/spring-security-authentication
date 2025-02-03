package nextstep.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.OncePerRequestFilter;

public abstract class CustomSecurityAuthFilter extends OncePerRequestFilter {
    abstract boolean match(final HttpServletRequest request);
}
