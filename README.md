# spring-security-authentication

1. LoginController 를 통한 인증 관리
 - /login URL 에  `MemberAuthorizationInterceptor` 를 적용
 - `MemberAuthorizationInterceptor` 는 세션에 이미 저장된 인증값이 있는 경우 바이패스 됨
 - 세션 인증값이 없는 경우 주어진 request parameter 로 repository 에서 `Member` 를 조회
 - 조회 결과가 있는 경우 세션 업데이트, 인증 처리 함


2. MemberController 를 통한 인가 관리
- /member URL 에 `BasicAuthenticationInterceptor` 를 적용
- `BasicAuthenticationInterceptor` 는 토큰을 받아 해당 토큰이 유효한지 여부를 판단
- 이 때 토큰을 decode 하기 위해 `BasicAuthenticationService` 에서 Base64 기준 토큰 분해 및 `Member` 객체에 담아서 리턴
- `BasicAuthenticationInterceptor` 는 토큰을 분해해서 얻은 Member 의 email 을 조회하여 인가 여부를 결정
- 토큰값이 유효하지 않은 경우 `InvalidTokenExcpetion` 발생
- 그 외 Interceptor 에서 무효 처리 (단순 false 리턴)

