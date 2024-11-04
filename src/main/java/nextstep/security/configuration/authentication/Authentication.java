package nextstep.security.configuration.authentication;

import java.io.Serializable;

public interface Authentication extends Serializable {

    Object getCredentials();

    Object getPrincipal();

    boolean isAuthenticated();

}
