package nextstep.security.user;

public interface UserDetailsService {

    UserDetails loadUserByUsername(String username);
}
