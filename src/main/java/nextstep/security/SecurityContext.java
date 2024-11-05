package nextstep.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextstep.security.authentication.Authentication;

@Setter
@Getter
@NoArgsConstructor
public class SecurityContext {

    private Authentication authentication;

    public SecurityContext(Authentication authentication) {
        this.authentication = authentication;
    }
}
