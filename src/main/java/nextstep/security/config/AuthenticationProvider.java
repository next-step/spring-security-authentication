package nextstep.security.config;

import nextstep.security.AuthenticationException;

public interface AuthenticationProvider {

    /**
     * AuthenticationManager의 authenticate 메서드와 같은 contract으로 인증 수행

     * @param authentication : 인증 요청 객체
     * @return credential을 포함한 완전히 authenticated 객체를 반환.
     * AuthenticationProvider가 받은 Authentication 객체에 대한 인증을 지원하지 않으면 지원null 을 반환 할 수 있음.
     * 그러면 Authentication을 지원하는 그 다음 AuthenticationProvider가 시도된다.
     */
    Authentication authenticate(Authentication authentication) throws AuthenticationException;

    /**
     *
     * AuthenticationProvider가 Authentication를 support 하면 true 반환
     * true를 반환하는것이 AuthenticationProvider가 인증할 수 있을것이라고 보장하는 것이 아님. 그냥 더 자세히 평가를 지원한다는 의미.
     * 다른 AuthenticationProvider를 시도해봐야한다는 의미로 AuthenticationProvider는 authenticate()결과를 null로 반환
     * 인증 가능한 AuthenticationProvider 선택은 런타임에 ProviderManager에 의해서 이루어짐
     *
     * @param authentication
     * @return <code>true</code> if the implementation can more closely evaluate the Authentication class presented
     */
    boolean supports(Class<?> authentication);

}
