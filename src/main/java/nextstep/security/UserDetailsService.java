package nextstep.security;

public interface UserDetailsService {
    UserDetails loadUserDetailsByUserName(String username) throws AuthenticationException;
}
