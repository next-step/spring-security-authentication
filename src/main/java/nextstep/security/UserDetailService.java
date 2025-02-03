package nextstep.security;

public interface UserDetailService {
    UserDetails loadUserDetailsByUserName(String username) throws AuthenticationException;
}
