package nextstep.security;

public class MemberDetail {
    String username;
    String password;

    public MemberDetail(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean isCorrectPassword(String password) {
        return this.password.equals(password);
    }
}
