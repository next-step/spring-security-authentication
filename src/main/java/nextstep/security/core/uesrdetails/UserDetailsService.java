package nextstep.security.core.uesrdetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
