package nextstep.security.core.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import nextstep.security.core.SecurityPrincipal;

@AllArgsConstructor
@Data
public class Authentication {
    private Object credential;
    private SecurityPrincipal principal;
    private boolean isAuthenticated;
}
