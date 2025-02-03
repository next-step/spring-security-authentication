# spring-security-authentication

## 🚀 1단계 - SecurityFilterChain 적용
- [x] 기존 Interceptor 인증 로직 변환
- [x] DelegatingFilterProxy 설정
  - Spring Bean으로 DelegatingFilterProxy를 등록하고 이를 통해 FilterChainProxy를 호출하도록 설정
  - DelegatingFilterProxy는 Servlet 컨테이너의 Filter와 Spring 컨텍스트를 연결
- [x] FilterChainProxy 구현
- [x] SecurityFilterChain 리팩터링

## 🚀 2단계 - AuthenticationManager 적용
- [x] Authentication와 UsernamePasswordAuthenticationToken 구현
- [x] 제공된 AuthenticationManager를 기반으로 ProviderManager 구현
- [x] 제공된 AuthenticationProvider를 기반으로 DaoAuthenticationProvider 구현
- [x] 기존 인증 필터에서 인증 로직 분리 및 AuthenticationManager로 통합
