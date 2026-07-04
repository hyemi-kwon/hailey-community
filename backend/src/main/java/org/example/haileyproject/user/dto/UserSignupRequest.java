package org.example.haileyproject.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserSignupRequest {

    /**
     * DTO 검증 어노테이션
     * @NotBlank: 값이 비어있거나 띄어쓰기만 있으면 안 된다고 설정합니다. (null, "", " " 모두 불가능)
     * @NotNull: 값이 아예 안 들어오면(null) 안 된다고 설정합니다. 숫자형(Long, Integer)에 주로 사용합니다.
     * @Email: 이메일 형식(예: abc@def.com)이 맞는지 검사합니다.
     * @Pattern(regexp = "..."): 정규표현식(Regular Expression)이라는 특수한 문자열을 사용해서, 값이 특정 규칙에 맞는지 검사합니다.
     * @Size(max = ...): 글자의 최대 길이나 범위를 제한합니다.
     * message 속성: 만약 조건을 어겼을 때 클라이언트에게 보낼 에러 메세지를 적어둡니다.
     */
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 주소 형식을 입력해주세요.(예 : example@example.com)")
    private String email;

    /**
     * DTO 검증 어노테이션
     * @NotBlank: 값이 비어있거나 띄어쓰기만 있으면 안 된다고 설정합니다. (null, "", " " 모두 불가능)
     * @NotNull: 값이 아예 안 들어오면(null) 안 된다고 설정합니다. 숫자형(Long, Integer)에 주로 사용합니다.
     * @Email: 이메일 형식(예: abc@def.com)이 맞는지 검사합니다.
     * @Pattern(regexp = "..."): 정규표현식(Regular Expression)이라는 특수한 문자열을 사용해서, 값이 특정 규칙에 맞는지 검사합니다.
     * @Size(max = ...): 글자의 최대 길이나 범위를 제한합니다.
     * message 속성: 만약 조건을 어겼을 때 클라이언트에게 보낼 에러 메세지를 적어둡니다.
     */
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "비밀번호는 8자 이상, 20자 이하이며, 대문자, 소문자, 숫자, 특수문자를 각각 최소 1개 포함해야 합니다.")
    private String password;

    /**
     * DTO 검증 어노테이션
     * @NotBlank: 값이 비어있거나 띄어쓰기만 있으면 안 된다고 설정합니다. (null, "", " " 모두 불가능)
     * @NotNull: 값이 아예 안 들어오면(null) 안 된다고 설정합니다. 숫자형(Long, Integer)에 주로 사용합니다.
     * @Email: 이메일 형식(예: abc@def.com)이 맞는지 검사합니다.
     * @Pattern(regexp = "..."): 정규표현식(Regular Expression)이라는 특수한 문자열을 사용해서, 값이 특정 규칙에 맞는지 검사합니다.
     * @Size(max = ...): 글자의 최대 길이나 범위를 제한합니다.
     * message 속성: 만약 조건을 어겼을 때 클라이언트에게 보낼 에러 메세지를 적어둡니다.
     */
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = 10, message = "닉네임은 최대10글자 까지 작성 가능합니다.")
    @Pattern(regexp = "^[^\\s]*$", message = "띄어쓰기를 없애주세요")
    private String nickname;

    /**
     * DTO 검증 어노테이션
     * @NotBlank: 값이 비어있거나 띄어쓰기만 있으면 안 된다고 설정합니다. (null, "", " " 모두 불가능)
     * @NotNull: 값이 아예 안 들어오면(null) 안 된다고 설정합니다. 숫자형(Long, Integer)에 주로 사용합니다.
     * @Email: 이메일 형식(예: abc@def.com)이 맞는지 검사합니다.
     * @Pattern(regexp = "..."): 정규표현식(Regular Expression)이라는 특수한 문자열을 사용해서, 값이 특정 규칙에 맞는지 검사합니다.
     * @Size(max = ...): 글자의 최대 길이나 범위를 제한합니다.
     * message 속성: 만약 조건을 어겼을 때 클라이언트에게 보낼 에러 메세지를 적어둡니다.
     */
    private String profileImage;
    private String profileImageUrl;

    public UserSignupRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImage() {
        return profileImage != null ? profileImage : profileImageUrl;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
