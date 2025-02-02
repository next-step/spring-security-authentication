package nextstep.security.model;

import nextstep.security.constants.Constants;
import org.springframework.util.Base64Utils;

import java.util.Objects;

public class EmailPasswordAuth {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public EmailPasswordAuth(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static EmailPasswordAuth from(String basicAuth) {
        if(Objects.isNull(basicAuth) || !basicAuth.startsWith(Constants.Auth.BASIC)) {
            return null;
        }
        String base64Credentials = basicAuth.substring(Constants.Auth.BASIC.length()).trim();
        try {
            // Base64 문자열을 디코딩
            byte[] decoded = Base64Utils.decodeFromString(base64Credentials);
            String credentials = new String(decoded);

            // 사용자 이름과 비밀번호가 ':'로 구분되는지 확인
            String[] parts = credentials.split(":", 2);
            if(parts.length == 2) {
                return new EmailPasswordAuth(parts[0], parts[1]);
            }
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static EmailPasswordAuth from(String email, String password) {
        if(email == null || password == null) {
            return null;
        }
        return new EmailPasswordAuth(email, password);
    }
}
