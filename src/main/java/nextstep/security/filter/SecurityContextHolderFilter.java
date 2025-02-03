package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.app.SecurityContextHolder;
import nextstep.security.SecurityContext;
import nextstep.security.SecurityContextRepository;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

public class SecurityContextHolderFilter extends GenericFilterBean {

    private final SecurityContextRepository contextRepository;

    public SecurityContextHolderFilter(SecurityContextRepository contextRepository) {
        this.contextRepository = Objects.requireNonNull(contextRepository);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest request) {
            SecurityContext context = contextRepository.loadContext(request);
            if (context != null) {
                SecurityContextHolder.setContext(context);
            }
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
