package nextstep.security.service;

import nextstep.app.domain.Member;

public interface UserDetailService {
    Member getMember(String username, String password);
}
