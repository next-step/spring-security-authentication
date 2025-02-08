package nextstep.security.authentication.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface SecurityContextRepository {

    SecurityContext loadContext(HttpServletRequest request);

    void saveContext(SecurityContext context, HttpServletRequest request);

}
