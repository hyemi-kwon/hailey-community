package org.example.haileyproject.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserLoginRequest {

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
    @Email(message = "올바른 이메일 주소 형식을 입력해주세요 (예:example@adapterz.kr)")
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
    private String password;

    public UserLoginRequest() {
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
}
