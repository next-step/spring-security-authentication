package nextstep.security.model.context;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import nextstep.security.model.authentication.Authentication;

@Getter
@Setter
public class SecurityContext implements Serializable {

    private Authentication authentication;

    public SecurityContext() {}

    public SecurityContext(Authentication authentication) {
        this.authentication = authentication;
    }

}
