package nextstep.app.domain;

import nextstep.security.UserDetails;

public class CustomMember implements UserDetails {
    private final Member member;

    public CustomMember(final Member member) {
        this.member = member;
    }

    @Override
    public String getUsername() {
        return member.getName();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }
}
