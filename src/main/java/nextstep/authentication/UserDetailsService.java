package nextstep.authentication;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
