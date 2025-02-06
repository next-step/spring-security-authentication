package nextstep.app.domain;

import nextstep.security.core.uesrdetails.UserDetails;
import nextstep.security.role.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class CustomMember implements UserDetails {
    private final Member member;
    private final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

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

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    public void addAuthority(GrantedAuthority grantedAuthority) {
        this.grantedAuthorities.add(grantedAuthority);
    }
}
