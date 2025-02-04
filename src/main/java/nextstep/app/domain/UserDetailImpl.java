package nextstep.app.domain;

import nextstep.security.UserDetail;

public class UserDetailImpl implements UserDetail {

    private final Member member;

    public UserDetailImpl(Member member) {
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
