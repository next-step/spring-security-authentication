package nextstep.security.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface SecurityContextRepository {
    SecurityContext loadContext(final HttpServletRequest request);
    void saveContext(final SecurityContext context, final HttpServletRequest request, final HttpServletResponse response);
}
