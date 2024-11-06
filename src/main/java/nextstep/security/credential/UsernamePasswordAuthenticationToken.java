package nextstep.security.credential;

import lombok.Data;
import nextstep.security.model.SecurityAuthentication;

@Data
public class UsernamePasswordAuthenticationToken implements SecurityAuthentication {
    private final Object principal;
    private final Object credentials;
    private final boolean authenticated;

    public static UsernamePasswordAuthenticationToken unauthenticated(String principal, String credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, false);
    }
    public static UsernamePasswordAuthenticationToken authenticated(String principal, String credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, true);
    }
}
