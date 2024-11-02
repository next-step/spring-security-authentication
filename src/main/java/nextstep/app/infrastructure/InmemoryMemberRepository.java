package nextstep.app.infrastructure;

import nextstep.app.domain.member.param.Member;
import nextstep.app.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InmemoryMemberRepository implements MemberRepository {
    public static final Member TEST_MEMBER_1 = new Member("a@a.com", "password", "a", "");
    public static final Member TEST_MEMBER_2 = new Member("b@b.com", "password", "b", "");
    private static final Map<String, Member> members = new HashMap<>();

    static {
        members.put(TEST_MEMBER_1.getEmail(), TEST_MEMBER_1);
        members.put(TEST_MEMBER_2.getEmail(), TEST_MEMBER_2);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(members.get(email));
    }

    @Override
    public List<Member> findAll() {
        return members.values().stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void save(Member member) {
        members.put(member.getEmail(), member);
    }
}
