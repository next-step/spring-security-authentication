package nextstep.app.domain;

import nextstep.security.domain.UserDetails;

public class UserDetailsImpl implements UserDetails {
    private final String email;
    private final String password;

    private UserDetailsImpl(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserDetailsImpl of(Member member) {
        return new UserDetailsImpl(member.getEmail(), member.getPassword());
    }

    public static UserDetailsImpl empty() {
        return new UserDetailsImpl(null, null);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEmpty() {
        return email == null && password == null;
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }
}
