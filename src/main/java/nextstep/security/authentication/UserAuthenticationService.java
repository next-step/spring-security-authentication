package nextstep.security.authentication;

public interface UserAuthenticationService {
    void validateMember(String principal, String credentials);
}