package nextstep.security.service;

public interface UserDetailsService {
    void authenticate(String username, String password);
}
