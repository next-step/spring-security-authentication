# spring-security-authentication


## 1 주차
<hr>

### 🚀 1단계 - SecurityFilterChain 적용
- [x] 기존 Interceptor 인증 로직 변환
- [x] DelegatingFilterProxy 설정
- [x] FilterChainProxy 구현
- [x] SecurityFilterChain 리팩터링

### 🚀 2단계 - AuthenticationManager 적용
- [x] Authentication와 UsernamePasswordAuthenticationToken 구현
- [x] 제공된 AuthenticationManager를 기반으로 ProviderManager 구현
- [x] 제공된 AuthenticationProvider를 기반으로 DaoAuthenticationProvider 구현
- [x] 기존 인증 필터에서 인증 로직 분리 및 AuthenticationManager로 통합