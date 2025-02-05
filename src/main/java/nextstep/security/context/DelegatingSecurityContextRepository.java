package nextstep.security.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;

public class DelegatingSecurityContextRepository implements SecurityContextRepository {
    private final List<SecurityContextRepository> delegates;

    public DelegatingSecurityContextRepository(SecurityContextRepository... delegates) {
        this.delegates = Arrays.asList(delegates);
    }

    @Override
    public SecurityContext loadContext(HttpServletRequest request) {
        for (SecurityContextRepository delegate : delegates) {
            SecurityContext context = delegate.loadContext(request);
            if (context != null) {
                return context;
            }
        }

        return null;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        for (SecurityContextRepository delegate : delegates) {
            delegate.saveContext(context, request, response);
        }
    }
}
