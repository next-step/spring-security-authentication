package nextstep.security.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 요청들 사이에 SecurityContext를 영속시키는데 사용되는 전략.
 *
 * SecurityContextPersistenceFilter에 의해 사용된다.
 * 현재 실행되는 스레드에 사용되는 context를 획득하고
 * 요청이 끝나고 thread-local 스토리지에서 일단 저장되면 저장하는데 사용된다.
 *
 * persistence 매커니즘이 구현에 따라 달라지지만, HttpSession을 사용해 context를 저장하는데 사용한다.
 */
public interface SecurityContextRepository {

    /**
     * 공급된 요청에서 security context를 획득한다.
     * 인증 안된 사용자에게는 빈 컨텍스트 impl이 반환된다.
     * null을 반환해서는 안된다.
     *
     */
    SecurityContext loadContext(HttpServletRequest request);

    /**
     * stores the security context on completion of a request.
     * request에 맞는 context가 찾아지면 true 아니면 false를 반환함
     */
    void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response);
}
