package nextstep.security.domain;

public interface UserDetailsService {
    UserDetails loadUserByUsernameAndPassword(String username, String password);
    UserDetails loadUserByUsername(String username);
}
