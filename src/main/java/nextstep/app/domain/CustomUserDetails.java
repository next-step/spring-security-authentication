package nextstep.app.domain;

import nextstep.security.user.UserDetails;
import org.springframework.util.StringUtils;

public record CustomUserDetails(String username, String password) implements UserDetails {
    public CustomUserDetails {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new IllegalArgumentException("username or password cannot be empty");
        }
    }

    public static UserDetails from(Member member) {
        return new CustomUserDetails(member.getName(), member.getPassword());
    }
}
