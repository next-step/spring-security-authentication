# spring-security-authentication

기능 요구 사항

아이디와 비밀번호를 기반으로 로그인 기능울 구현, Basic 인증을 사용하여 사용자를 식별 할 수 있도록 프레임워크 사용.

웹앱으로 구현

아이디, 비밀번호 기반 로그인 구현
- POST /login 경로로 로그인 요청
- 사용자의 아이디 비밀번호르 확인하여 인증
- 로그인 성공시 session을 사용해 인증 정보 저장
- LoginTest의 모든 테스트가 통과해야함.

Basic 인증 구현
- GET /member 요청 시 사용자 목록을 조회
- 단 member로 등록되어 있느 ㄴ사용자만 간으하도록
- 이를 위해 basic 인증을 사용해 사용자 식별
- Authorization 헤더에서 Basic 인증정보를 추출하여 인증처리
- 인증 성공 시 session에 에 인증 정보 저장
- MemberTest의 모든 테스트가 통과