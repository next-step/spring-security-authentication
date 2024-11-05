package nextstep.security.service;

import nextstep.security.model.UserDetails;


public interface UserDetailsService {
     UserDetails loadUserByUsername(String username);

}
