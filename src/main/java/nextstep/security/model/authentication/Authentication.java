package nextstep.security.model.authentication;

import java.io.Serializable;

public interface Authentication extends Serializable {

    Object getCredentials();

    Object getPrincipal();

    boolean isAuthenticated();

}
