package nextstep.app.service;

public class UserDetail {

    private final String email;
    private final String password;

    public UserDetail(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
