package nextstep.security.core.userdetails;

public interface UserDetailService {
    UserDetail findUserByUsername(String username);
}
