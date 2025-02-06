package nextstep.security.domain;

public interface MemberDetail {
    String username();
    String password();
    boolean isCorrectPassword(String password);
}
