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

## 진행 중 놓친 부분

> FilterChainProxy, DefaultSecurityFilterChain, SecurityFilterChain\

### 위 클래스들을 통한 구조의 핵심:
보안 필터들을 __여러 그룹으로__ 나누고, \
요청에 따라 적절한 그룹의 필터들을 __순서대로__ 실행할 수 있는 구조

### FilterChainProxy 의 역할:
[FilterChainProxy.java](./src/main/java/nextstep/security/filter/config/FilterChainProxy.java)\
필터들의 실행을 관리하는 중앙 컨트롤러
VirtualFilterChain 을 통해 필터들을 순차적으로 실행
``` java
    if (this.currentPosition == this.size) {
        this.originalChain.doFilter(request, response);
        return;
    }
    
    this.currentPosition++;
    
    Filter nextFilter = this.additionalFilters.get(this.currentPosition - 1);
```

이 코드로 필터들이 순서대로 실행되고, 모든 필터가 실행된 후에 원래 체인으로 넘어감


### SecurityFilterChain의 의미:

요청에 따라 다른 필터 체인을 적용할 수 있는 구조

``` java
    if (chain.matches(request)) {
        return chain.getFilters();
    }
```

예를 들어:
 
        /api/** 경로는 JWT 인증 필터 체인
        /admin/** 경로는 Basic 인증 필터 체인

### VirtualFilterChain 의 역할:

필터들을 순차적으로 실행하되, 모든 필터가 실행된 후에는 원래의 필터 체인으로 제어를 넘김\
마치 하나의 필터처럼 동작하면서 내부적으로는 여러 필터를 순차 실행


🚀 4단계 - SecurityContextHolderFilter 구현

- [x] SecurityContextRepository 인터페이스를 기반으로 HttpSessionSecurityContextRepository 구현
- [x] SecurityContextHolderFilter 작성 및 필터 체인에 등록
- [x] login_after_members 테스트로 동작 검증