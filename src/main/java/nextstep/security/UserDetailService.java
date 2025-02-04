package nextstep.security;

public interface UserDetailService {

    UserDetail loadUserByUsername(String username);
}
