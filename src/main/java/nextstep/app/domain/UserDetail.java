package nextstep.app.domain;

import nextstep.security.user.UserDetails;

public class UserDetail implements UserDetails {

    private final String user;
    private final String password;

    public UserDetail(final String user,
                      final String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.user;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
