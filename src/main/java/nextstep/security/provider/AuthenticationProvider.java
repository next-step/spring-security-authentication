package nextstep.security.provider;

import nextstep.security.credential.SecurityCredential;
import nextstep.security.model.SecurityAuthentication;

public interface AuthenticationProvider {
    SecurityAuthentication authenticate(SecurityCredential credential);
}
