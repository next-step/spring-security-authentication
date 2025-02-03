package nextstep.app.domain;

import nextstep.security.user.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

public record CustomUserDetails(String username, String password,
                                Collection<Object> authorities) implements UserDetails {
    public CustomUserDetails {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new IllegalArgumentException("username or password cannot be empty");
        }
    }

    public static UserDetails from(Member member) {
        List<Object> authorities = member.getMemberRoleNames()
                .stream()
                .map(Object.class::cast)
                .toList();

        return new CustomUserDetails(member.getName(), member.getPassword(), authorities);
    }
}
