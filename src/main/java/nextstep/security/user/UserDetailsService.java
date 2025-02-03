package nextstep.security.user;

import nextstep.security.authentication.exception.AuthenticationException;

public interface UserDetailsService {
    UserDetails loadUserDetailsByUserName(String username) throws AuthenticationException;
}
