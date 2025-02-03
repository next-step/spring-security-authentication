package nextstep.app.domain;

import java.util.Set;
import java.util.stream.Collectors;

public class Member {
    private final String email;
    private final String password;
    private final String name;
    private final String imageUrl;
    private final Set<MemberRole> memberRole;

    public Member(String email, String password, String name, String imageUrl, MemberRole memberRole) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imageUrl = imageUrl;
        this.memberRole = Set.of(memberRole);
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

    public Set<String> getMemberRoleNames() {
        return memberRole.stream().map(MemberRole::name).collect(Collectors.toSet());
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}
