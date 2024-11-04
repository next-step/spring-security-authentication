package nextstep.app.domain;

import lombok.Data;
import nextstep.security.model.UserDetails;

@Data
public class Member {
    private final String email;
    private final String password;
    private final String name;
    private final String imageUrl;

    public UserDetails getUserDetails() {
        return new UserDetails(this.email, this.password);
    }
}
