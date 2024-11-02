# spring-security-authentication
### 아이디와 비밀번호 기반 로그인 구현
    - 아이디와 비밀번호 확인 기능
    - session 에 인증정보 저장

### Basic 인증 구현
    - Basic 유저 정보 추출
    - 인증 기능
    - session 에 인증정보 저장

### 인터셉터 분리
    - LoginController에서 IdPasswordAuthInterceptor로 인증 방식 분리
    - MemeberController에서 BasicAuthInterceptor로 인증 방식 분리