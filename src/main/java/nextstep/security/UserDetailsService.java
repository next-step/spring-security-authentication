package nextstep.security;

import nextstep.security.exception.AuthenticationException;

public interface UserDetailsService {
    UserDetails loadUserDetailsByUserName(String username) throws AuthenticationException;
}
