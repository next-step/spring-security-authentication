package nextstep.app.domain;

import lombok.Data;

@Data
public class Member {
    private final String email;
    private final String password;
    private final String name;
    private final String imageUrl;
}
