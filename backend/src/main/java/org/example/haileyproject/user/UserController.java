package org.example.haileyproject.user;

import org.example.haileyproject.common.ApiResponse;
import org.example.haileyproject.user.dto.UserInfoUpdateRequest;
import org.example.haileyproject.user.dto.UserPasswordUpdateRequest;
import org.example.haileyproject.user.dto.UserSignupRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.haileyproject.common.auth.SessionAuthInterceptor;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<Map<String, Long>> signup(@Valid @RequestBody UserSignupRequest request) {
        Map<String, Long> data = userService.signup(request);
        return ApiResponse.success("회원가입 성공", data);
    }

    @GetMapping("/email/check")
    public ApiResponse<Void> checkEmail(@RequestParam String email) {
        if (!userService.isEmailAvailable(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.");
        }
        return ApiResponse.success("사용 가능한 이메일입니다.", null);
    }

    @GetMapping("/nickname/check")
    public ApiResponse<Void> checkNickname(@RequestParam String nickname) {
        if (!userService.isNicknameAvailable(nickname)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다.");
        }
        return ApiResponse.success("사용 가능한 닉네임입니다.", null);
    }

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Map<String, Object>> updateMe(
            HttpServletRequest httpRequest,
            @Valid @RequestBody UserInfoUpdateRequest request) {
        Long userId = getSessionUserId(httpRequest);
        userService.updateInfoWithoutPassword(userId, request);
        return ApiResponse.success("회원정보가 수정되었습니다.", userService.getUserInfo(userId));
    }

    @DeleteMapping("/me")
    public ApiResponse<Map<String, Long>> withdrawMe(HttpServletRequest httpRequest) {
        Long userId = getSessionUserId(httpRequest);
        userService.withdraw(userId);
        Map<String, Long> data = new HashMap<>();
        data.put("userId", userId);
        return ApiResponse.success("유저 탈퇴 성공", data);
    }

    @PatchMapping("/me/password")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> updateMyPassword(
            HttpServletRequest httpRequest,
            @RequestBody Map<String, String> request) {
        Long userId = getSessionUserId(httpRequest);
        userService.updatePasswordWithoutCurrentPassword(userId, request.get("password"));
        return ApiResponse.success("비밀번호가 수정되었습니다.", null);
    }

    @PostMapping("/upload/profile-image")
    public ApiResponse<Map<String, String>> uploadProfileImage(@RequestParam("profileImage") MultipartFile file) {
        String fileName = file == null ? "profile" : file.getOriginalFilename();
        return ApiResponse.success("프로필 이미지 업로드 성공", Map.of("profileImageUrl", "/uploads/" + fileName));
    }

    @PatchMapping("/{userId}")
    public ApiResponse<Map<String, Long>> updateInfo(
            @PathVariable Long userId,
            @Valid @RequestBody UserInfoUpdateRequest request) {
        userService.updateInfo(userId, request);
        Map<String, Long> data = new HashMap<>();
        data.put("userId", userId);
        return ApiResponse.success("회원정보가 수정되었습니다.", data);
    }

    @PatchMapping("/{userId}/password")
    public ApiResponse<Void> updatePassword(
            @PathVariable Long userId,
            @Valid @RequestBody UserPasswordUpdateRequest request) {
        userService.updatePassword(userId, request);
        return ApiResponse.success("비밀번호가 수정되었습니다.", null);
    }

    @PatchMapping("/{userId}/withdraw")
    public ApiResponse<Map<String, Long>> withdraw(@PathVariable Long userId) {
        userService.withdraw(userId);
        Map<String, Long> data = new HashMap<>();
        data.put("userId", userId);
        return ApiResponse.success("유저 탈퇴 성공", data);
    }

    private Long getSessionUserId(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute(SessionAuthInterceptor.SESSION_KEY) == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }
        return (Long) session.getAttribute(SessionAuthInterceptor.SESSION_KEY);
    }
}
