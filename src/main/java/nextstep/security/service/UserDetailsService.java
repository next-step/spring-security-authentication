package nextstep.security.service;

import java.util.Optional;

import nextstep.security.model.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface UserDetailsService {


    Optional<UserDetails> loadUserByUsername(String username);
    Optional<UserDetails> loadUserByUsernameAndEmail(String username, String email);

}
