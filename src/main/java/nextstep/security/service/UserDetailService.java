package nextstep.security.service;

import nextstep.security.model.UserDetails;

public interface UserDetailService {
    UserDetails getUserDetails(String username, String password);
}
