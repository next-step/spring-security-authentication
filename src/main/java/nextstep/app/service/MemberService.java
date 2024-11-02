package nextstep.app.service;

import nextstep.app.domain.Member;

import java.util.List;

public interface MemberService {
    Member getMember(String username, String password);

    List<Member> findAll();
}
