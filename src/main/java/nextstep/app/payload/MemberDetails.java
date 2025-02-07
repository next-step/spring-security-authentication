package nextstep.app.payload;

import nextstep.app.domain.Member;
import nextstep.security.userdetails.UserDetails;

public class MemberDetails implements UserDetails {

    private final String principal;
    private final String credentials;

    public MemberDetails(final String principal, final String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    public static MemberDetails from(final Member member) {
        return new MemberDetails(member.getEmail(), member.getPassword());
    }

    @Override
    public String getPrincipal() {
        return this.principal;
    }

    @Override
    public String getCredentials() {
        return this.credentials;
    }

    @Override
    public boolean match(final Object credentials) {
        return this.credentials.equals(credentials);
    }

    @Override
    public String toString() {
        return "MemberDetails{" +
                "principal='" + principal + '\'' +
                ", credentials='" + credentials + '\'' +
                '}';
    }
}
