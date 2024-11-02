package nextstep.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailService {
    private final MemberRepository memberRepository;

    @Override
    public Member getMember(String username, String password) {
        Member member = memberRepository.findByEmail(username).orElse(null);

        if (member == null) {
            log.warn("Member is not exists");
            return null;
        }

        if (!StringUtils.equals(member.getPassword(), password)) {
            log.warn("Member is not valid");
            return null;
        }

        return member;
    }
}
