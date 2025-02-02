package nextstep.security.service;

public interface MemberValidationService {
    boolean isValidMember(String email, String password);
}
