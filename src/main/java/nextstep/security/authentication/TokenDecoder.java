package nextstep.security.authentication;

import nextstep.security.userdetail.UserDetail;

public interface TokenDecoder {

    UserDetail decodeToken(String token);
}
