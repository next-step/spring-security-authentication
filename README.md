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

### 🚀 3단계 - SecurityContextHolder 적용 

- [x] SecurityContext 및 SecurityContextHolder 작성
- [x] BasicAuthenticationFilter에서 SecurityContextHolder 활용
- [x] 기존 세션 방식에서 스레드 로컬 방식으로 인증 정보 관리 변경