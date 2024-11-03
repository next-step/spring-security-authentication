package nextstep.app.domain;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Member implements Serializable {
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

}
