package nextstep.util;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthenticationFixture {

    public static Authentication createAuthentication() {
        return new UsernamePasswordAuthenticationToken(
            "TEST", "TEST"
        );
    }

}
