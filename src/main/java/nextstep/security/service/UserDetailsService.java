package nextstep.security.service;

import nextstep.security.param.UserDetails;

public interface UserDetailsService {
    UserDetails retrieveMemberByEmailAndPassword(String email, String password);
}
