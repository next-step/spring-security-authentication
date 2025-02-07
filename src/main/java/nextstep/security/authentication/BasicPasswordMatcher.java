package nextstep.security.authentication;

public class BasicPasswordMatcher implements PasswordMatcher {
    @Override
    public boolean matches(String password1, String password2) {
        return password1.equals(password2);
    }
}
