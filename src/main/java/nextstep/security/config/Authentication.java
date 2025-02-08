package nextstep.security.config;


import java.io.Serializable;
import java.security.Principal;

/**
 * 일단 요청이  AuthenticationManager의 authenticate(Authentication) 메서드에 의해서 진행된다면
 * 인증 요청이나 authenticated principaldㅔ 대한 토큰을 나타낸다.

 * 일단 request가 authenticated 되면, 이 Authentication 객체는 SecurityContextHolder의 SecurityContext의 threadlocal에 저장된다.
 * spring security의 authentication 메커니즘을 사용하지 않고 아래와 같이 Authentication 인스턴스를 생성해서 명시적으로 사용가능하다
 * <pre>
 * SecurityContext context = SecurityContextHolder.createEmptyContext();
 * context.setAuthentication(anAuthentication);
 * SecurityContextHolder.setContext(context);
 * </pre>
 *
 * Authentication 객체가 authenticated 프로퍼티 값이 true로 지정되지 않는한,
 * 만나는 security interceptor마다 인증된다.

 * 대부분의 경우에 framework가 security context와 Authentication 객체를 를 투명하게 관리해줄것이다.
 **/
public interface Authentication extends Principal, Serializable {

    /**
     * principal이 올바른지 확인하는 crendentials.
     * 주로 password이나 AuthenticationManager와 관련된 어느것이든 가능하다.
     * caller가 이 credentials를 채운다.
     *
     * @return the credentials that prove the identity of the principal
     */
    Object getCredentials();

    /**
     * 인증 요청에 대한 추가적인 details를 저장한다.
     * IP 주소나 인증서 일련번호 등
     *
     * @return 인증 요청에 대한 추가적인 details. 사용하지 않으면 null
     */
    Object getDetails();

    /**
     *
     * 인증되는 principal(주체)의 신원.
     * username / password로 인증 요청의 경우, username이 된다.
     * AuthenticationManager implementation 은 더많은 정보를 가지고 있는 Authentication를 principal로 반환한다.
     * UserDetails 객체를 principal로 사용
     * @return 인증의 대상인 Principal이나 인증된 Principal
     */
    Object getPrincipal();

    /**
     * AbstractSecurityInterceptor가 인증 토큰을 AuthenticationManager에게 제시해야 하는지 여부를 나타내는 데 사용된다.
     * 일반적으로 AuthenticationManager (AuthenticationProvider중 하나) 는 성공적인 인증 후 불변 인증 토큰을 반환하며,
     * 그 경우 토큰이 안전하게 이 method에 true로 반환할 수 있다.
     * true를 반환하는 것은 performance를 높이고 AuthenticationManager를 매 요청마다 호출하는것은 더이상 불필요하게 된다.
     *
     * 보안적인 이유로 이 인터페이스 구현체는 불변이거나 처음 생성때부터 변하지 않는 프로퍼티를 보장하는 방법이 있지 않은한 true를 반환하는 것에 매우 주의해야 한다.
     *
     * @return true if the token has been authenticated and the
     * <code>AbstractSecurityInterceptor</code> does not need to present the token to the
     * <code>AuthenticationManager</code> again for re-authentication.
     */
    boolean isAuthenticated();

    /**
     * <p>
     * Implementations should <b>always</b> allow this method to be called with a
     * <code>false</code> parameter, as this is used by various classes to specify the
     * authentication token should not be trusted. If an implementation wishes to reject
     * an invocation with a <code>true</code> parameter (which would indicate the
     * authentication token is trusted - a potential security risk) the implementation
     * should throw an {@link IllegalArgumentException}.
     * @param isAuthenticated 는 토큰을 신뢰해야하는 경우 일때 true를 반환한다.
     * 토큰을 신뢰하지 말아야하는 경우 false를 반환한다.
     * @throws IllegalArgumentException |  authentication token을 신뢰하도록 만드는 시도가 실패했을경우 IllegalArgumentException 발생
     */
    void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;

}
