package nextstep.app.domain;

import nextstep.app.ui.AuthenticationException;

public class Member {
    private final String email;
    private final String password;
    private final String name;
    private final String imageUrl;

    public Member(String email, String password, String name, String imageUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void checkPassword(final String password) {
        if (!this.password.equals(password)) {
            throw new AuthenticationException();
        }
    }
}
