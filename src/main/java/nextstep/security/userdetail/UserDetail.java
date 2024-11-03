package nextstep.security.userdetail;

public class UserDetail {

    private final String username;

    private final String password;

    public UserDetail(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }
}
