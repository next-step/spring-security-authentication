package nextstep.app.domain.member.param;

import nextstep.security.param.UserDetails;

public class Member implements UserDetails {
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

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
