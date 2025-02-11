package nextstep.security.config;

public interface SecurityContext {

    /**
     * 최근에 인증된 principal을 얻거나 authentication request token을 얻는다.
     * @return Authentication이나 null을 반환. 인증 정보가 없다면
     */
    Authentication getAuthentication();

    void setAuthentication(Authentication authentication);

}
