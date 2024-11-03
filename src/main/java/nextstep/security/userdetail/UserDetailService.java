package nextstep.security.userdetail;

public interface UserDetailService {

    UserDetail getUserDetail(String username, String password);
}
