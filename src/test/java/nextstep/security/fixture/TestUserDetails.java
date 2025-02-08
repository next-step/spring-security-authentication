package nextstep.security.fixture;

import nextstep.security.UserDetails;

public class TestUserDetails implements UserDetails {
    private final String username;
    private final String password;

    public TestUserDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
