package nextstep.security.authentication;

import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.exception.AuthenticationException;
import nextstep.security.authentication.exception.MemberAccessDeniedException;

import java.io.IOException;

public class AuthenticationErrorHandler {

    protected AuthenticationErrorHandler() {
        throw new UnsupportedOperationException();
    }

    public static void handleError(HttpServletResponse response, RuntimeException e) throws IOException {
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
