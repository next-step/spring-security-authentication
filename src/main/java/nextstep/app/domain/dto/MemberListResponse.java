package nextstep.app.domain.dto;

import nextstep.app.domain.Member;

public class MemberListResponse {
    private final String email;
    private final String name;
    private final String imageUrl;

    public MemberListResponse(String email, String name, String imageUrl) {
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static MemberListResponse of(Member member){
        return new MemberListResponse(
                member.getEmail(), member.getName(), member.getImageUrl()
        );
    }
}
