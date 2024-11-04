package nextstep.security.core;

public interface UserDetailService {
    UserDetail findUserByUsername(String username);
}
