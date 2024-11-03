package nextstep.app.infrastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.springframework.stereotype.Repository;

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
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return members.values()
                .stream()
                .filter(member -> member.getEmail().equals(email) && member.getPassword().equals(password))
                .findFirst();
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
