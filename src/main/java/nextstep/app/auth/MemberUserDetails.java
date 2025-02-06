package nextstep.app.auth;

import nextstep.app.domain.Member;
import nextstep.security.user.UserDetails;

public class MemberUserDetails implements UserDetails {
    private final Member member;

    public MemberUserDetails(Member member) {
        this.member = member;
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }
}
