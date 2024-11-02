package nextstep.app.domain.member.repository;

import nextstep.app.domain.member.param.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);

    List<Member> findAll();

    void save(Member member);
}
