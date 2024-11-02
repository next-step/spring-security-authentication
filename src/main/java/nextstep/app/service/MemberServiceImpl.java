package nextstep.app.service;

import lombok.RequiredArgsConstructor;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Override
    public Member getMember(String username, String password) {
        Member member = memberRepository.findByEmail(username).orElse(null);

        if (member == null) {
            throw new AuthenticationException();
        }

        if (!StringUtils.equals(member.getPassword(), password)) {
            throw new AuthenticationException();
        }

        return member;
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
