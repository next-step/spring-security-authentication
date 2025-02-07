package nextstep.security.context;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class SecurityContextHolderFilter extends GenericFilterBean {

    private final SecurityContextRepository securityContextRepository;

    public SecurityContextHolderFilter(final SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) request;
        var httpServletResponse = (HttpServletResponse) response;
        var context = securityContextRepository.loadContext(httpServletRequest);
        SecurityContextHolder.setContext(context);
        try {
            chain.doFilter(request, response);
            securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

}
