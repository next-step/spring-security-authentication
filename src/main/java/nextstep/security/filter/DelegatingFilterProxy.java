package nextstep.security.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

@AllArgsConstructor
public class DelegatingFilterProxy extends GenericFilterBean {

    Filter delegate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        delegate.doFilter(request, response, chain);
    }
}
