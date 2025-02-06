package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolderStrategy;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.function.Supplier;

public class SecurityContextHolderFilter extends GenericFilterBean {

    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();
    private final SecurityContextRepository securityContextRepository;

    public SecurityContextHolderFilter(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Supplier<SecurityContext> securityContextSupplier = () -> securityContextRepository.loadContext(request);

        try {
            securityContextHolderStrategy.setDeferredContext(securityContextSupplier);
            filterChain.doFilter(request, response);
        } finally {
            securityContextHolderStrategy.clearContext();
        }
    }
}
