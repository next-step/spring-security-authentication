package nextstep.security.role;

import java.util.Objects;

public class Role implements GrantedAuthority {
    public static final String ROLE_PREFIX = "ROLE_";
    private final String roleName;

    public Role(String roleName) {
        this.roleName = ROLE_PREFIX + roleName.toUpperCase();
    }

    @Override
    public String getAuthority() {
        return roleName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Role)) return false;
        Role other = (Role) obj;
        return Objects.equals(roleName, other.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName);
    }

    @Override
    public String toString() {
        return roleName;
    }
}
