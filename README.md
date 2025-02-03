# spring-security-authentication

## 🚀 1단계 - SecurityFilterChain 적용
- [x] 기존 Interceptor 인증 로직 변환
- [x] DelegatingFilterProxy 설정
  - Spring Bean으로 DelegatingFilterProxy를 등록하고 이를 통해 FilterChainProxy를 호출하도록 설정
  - DelegatingFilterProxy는 Servlet 컨테이너의 Filter와 Spring 컨텍스트를 연결
- [x] FilterChainProxy 구현
- [ ] SecurityFilterChain 리팩터링
