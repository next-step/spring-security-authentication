package nextstep.security.service;

import nextstep.security.model.UserDetail;


public interface UserDetailService {
     UserDetail loadUserByUsername(String username);

}
