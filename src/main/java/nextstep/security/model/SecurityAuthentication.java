package nextstep.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecurityAuthentication {
    private UserDetails userDetails;
    private boolean isAuthenticated;
}
