package nextstep.security.service;

import nextstep.security.model.UserDetail;


public interface UserDetailService {
     UserDetail isValidUser(String email, String password);
}
