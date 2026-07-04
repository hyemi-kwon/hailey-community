package org.example.haileyproject.post.dto;

import jakarta.validation.constraints.NotBlank;

public class PostCreateRequest {
    
    /**
     * DTO 검증 어노테이션
     * @NotBlank: 값이 비어있거나 띄어쓰기만 있으면 안 된다고 설정합니다. (null, "", " " 모두 불가능)
     * @NotNull: 값이 아예 안 들어오면(null) 안 된다고 설정합니다. 숫자형(Long, Integer)에 주로 사용합니다.
     * @Email: 이메일 형식(예: abc@def.com)이 맞는지 검사합니다.
     * @Pattern(regexp = "..."): 정규표현식(Regular Expression)이라는 특수한 문자열을 사용해서, 값이 특정 규칙에 맞는지 검사합니다.
     * @Size(max = ...): 글자의 최대 길이나 범위를 제한합니다.
     * message 속성: 만약 조건을 어겼을 때 클라이언트에게 보낼 에러 메세지를 적어둡니다.
     */
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    /**
     * DTO 검증 어노테이션
     * @NotBlank: 값이 비어있거나 띄어쓰기만 있으면 안 된다고 설정합니다. (null, "", " " 모두 불가능)
     * @NotNull: 값이 아예 안 들어오면(null) 안 된다고 설정합니다. 숫자형(Long, Integer)에 주로 사용합니다.
     * @Email: 이메일 형식(예: abc@def.com)이 맞는지 검사합니다.
     * @Pattern(regexp = "..."): 정규표현식(Regular Expression)이라는 특수한 문자열을 사용해서, 값이 특정 규칙에 맞는지 검사합니다.
     * @Size(max = ...): 글자의 최대 길이나 범위를 제한합니다.
     * message 속성: 만약 조건을 어겼을 때 클라이언트에게 보낼 에러 메세지를 적어둡니다.
     */
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    /**
     * DTO 검증 어노테이션
     * @NotBlank: 값이 비어있거나 띄어쓰기만 있으면 안 된다고 설정합니다. (null, "", " " 모두 불가능)
     * @NotNull: 값이 아예 안 들어오면(null) 안 된다고 설정합니다. 숫자형(Long, Integer)에 주로 사용합니다.
     * @Email: 이메일 형식(예: abc@def.com)이 맞는지 검사합니다.
     * @Pattern(regexp = "..."): 정규표현식(Regular Expression)이라는 특수한 문자열을 사용해서, 값이 특정 규칙에 맞는지 검사합니다.
     * @Size(max = ...): 글자의 최대 길이나 범위를 제한합니다.
     * message 속성: 만약 조건을 어겼을 때 클라이언트에게 보낼 에러 메세지를 적어둡니다.
     */
    private Long userId;

    private String image;
    private String attachFileUrl;

    public PostCreateRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAttachFileUrl() {
        return attachFileUrl != null ? attachFileUrl : image;
    }

    public void setAttachFileUrl(String attachFileUrl) {
        this.attachFileUrl = attachFileUrl;
    }
}
