package nextstep.security.service;

import nextstep.security.userdetails.UserDetails;

public interface UserDetailsService {

    /**
     * 사용자 이름을 기반으로 사용자 정보를 가져온다.
     *
     * @param email    사용자 이메일
     * @param password 사용자 비밀번호
     * @return 사용자 정보
     */
    UserDetails loadUserByEmailAndPassword(String email, String password);
}
