package org.example.haileyproject.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserPasswordUpdateRequest {

    @NotBlank(message = "현재 비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "비밀번호는 8~15자이며, 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String newPassword;

    public UserPasswordUpdateRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
