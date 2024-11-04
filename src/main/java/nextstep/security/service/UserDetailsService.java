package nextstep.security.service;

import java.util.Optional;

import nextstep.app.domain.MemberRepository;
import nextstep.security.model.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface UserDetailsService {

    MemberRepository memberRepository = null;

    Optional<UserDetails> loadUserByUsername(String username);
    Optional<UserDetails> loadUserByUsernameAndEmail(String username, String email);

}
