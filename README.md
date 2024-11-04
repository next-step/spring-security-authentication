# spring-security-authentication

## 인증과 서비스 로직간의 분리

- [x] 인증은 security 패키지에 위치해야한다.
- [x] 서비스는 app 패키지에 위치해야한다.

## 기능 요구 사항

1. 아이디와 비밀번호 기반의 로그인 기능 구현
2. Basic 인증을 사용하여 사용자를 식별할 수 있도록 스프링 프래임워크를 통한 웹 앱 구현

### 아이디와 비밀번호 기반 로그인 구현

- [x] 사용자가 입력한 아이디와 비밀번호를 확인하여 인증한다.
- [x] 로그인 성공시 Session 을 사용하여 인증 정보를 저장한다.
- [x] LoginTest 의 모든 테스트가 성공해야한다.

### Basic 인증 구현

- [x] 요청의 Authorization 헤더에 Basic 인증 정보를 추출 하여 인증을 추출한다.
- [x] 인증을 성공한 경우 Session 을 사용하여 인증 정보를 저장한다.
- [x] MemberTest 의 모든 테스트가 통과해야한다.

## 프로그래밍 요구사항

- [x] 자바 코드 컨벤션을 준수한다.
- [x] 들여쓰기는 depth 가 3 이 넘지 않도록 한다.
- [x] 3항 연산자를 사용하지 않는다.
- [x] 함수의 길이가 15 라인을 넘지 않도록 한다.
- [x] else 예약어를 사용하지 않는다.
- [x] 정리한 기능 목록이 정상적으로 동작하는지 테스트 코드를 구현한다.

# 페어 코딩

- `SecurityFilterChain` : 보안 필터의 묶음을 정의하는 인터페이스
- `DefaultSecurityFilterChain` : `SecurityFilterChain` 의 구현체
- `FilterChainProxy` : 보안 필터의 묶음을 관리하는 객체
- `DelegatingFilterProxy` : 스프링에 등록한 필터를 실행을 위임할 객체

패키지 분리

## 1. Interceptor 에서 Filter 로 변경하기

### `GenericFilterBean` 와 `OncePerRequestFilter` 의 차이점

실행 횟수: `GenericFilterBean` 은 요청마다 실행될 수 있지만, `OncePerRequestFilter` 는 요청당 한 번만 실행됩니다.
사용 목적: 요청별로 한 번만 실행되어야 하는 필터링 로직에는 `OncePerRequestFilter` 가 적합하며, 그렇지 않으면 `GenericFilterBean` 을 사용할
수 있습니다.

- [x] `BasicAuthorizationInterceptor` -> `BasicAuthenticationFilter`
- [x] `FormLoginAuthorizationInterceptor` -> `UsernamePasswordAuthenticationFilter`
- [x] `WebMvcConfigurer` -> `SecurityConfig` 로 변경

## 2. AuthenticationManager 로 인증 추상화 하기

- [x] `AuthenticationManager` 구현
- [x] `ProviderManager` 구현
- [x] `AuthenticationProvider` 구현
- [x] `DaoAuthenticationProvider` 구현

## SecurityContextHolder 로 인증 정보 객체 저장하기

## SecurityContextHolderFilter 구현하기

