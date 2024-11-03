package nextstep.security.dto;

public class MemberDTO {
    private final String email;
    private final String password;
    private final String name;
    private final String imageUrl;

    public MemberDTO(String email, String password, String name, String imageUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
