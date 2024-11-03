package nextstep.app.domain;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndPassword(String email, String password);

    List<Member> findAll();

    void save(Member member);
}
