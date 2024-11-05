package nextstep.security.util;

import nextstep.security.userdetail.UserDetail;

public interface TokenDecoder {

    UserDetail decodeToken(String token);
}
