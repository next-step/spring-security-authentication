package nextstep.security;

public interface UserDetailService {

    UserDetails getUserByUsername(String username);

}
