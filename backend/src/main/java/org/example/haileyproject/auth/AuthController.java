package org.example.haileyproject.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.haileyproject.common.ApiResponse;
import org.example.haileyproject.common.auth.SessionAuthInterceptor;
import org.example.haileyproject.user.UserService;
import org.example.haileyproject.user.dto.UserLoginRequest;
import org.example.haileyproject.user.dto.UserSignupRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody UserLoginRequest request, HttpServletRequest httpRequest) {
        return loginWithPath(request, httpRequest);
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> loginWithPath(@Valid @RequestBody UserLoginRequest request, HttpServletRequest httpRequest) {
        Map<String, Object> data = userService.login(request);

        // 로그인 성공 시 세션 생성
        HttpSession session = httpRequest.getSession();
        session.setAttribute(SessionAuthInterceptor.SESSION_KEY, data.get("userId"));

        return ApiResponse.success("로그인성공", data);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public ApiResponse<Map<String, Long>> signup(@Valid @RequestBody UserSignupRequest request) {
        return ApiResponse.success("회원가입 성공", userService.signup(request));
    }

    @GetMapping("/check")
    public ApiResponse<Map<String, Object>> check(HttpServletRequest httpRequest) {
        Long userId = getSessionUserId(httpRequest);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }

        return ApiResponse.success("인증 확인 성공", userService.getUserInfo(userId));
    }

    @PostMapping("/logout")
    public ApiResponse<Map<String, Long>> logoutByPost(HttpServletRequest httpRequest) {
        return logout(httpRequest);
    }

    @DeleteMapping("/logout")
    public ApiResponse<Map<String, Long>> logoutByDeletePath(HttpServletRequest httpRequest) {
        return logout(httpRequest);
    }

    @DeleteMapping
    public ApiResponse<Map<String, Long>> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        Long userId = null;
        if (session != null) {
            userId = (Long) session.getAttribute(SessionAuthInterceptor.SESSION_KEY);
            session.invalidate();
        }

        Map<String, Long> data = new HashMap<>();
        if (userId != null) {
            data.put("userId", userId);
        }

        return ApiResponse.success("유저 로그아웃 성공", data);
    }

    private Long getSessionUserId(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            return null;
        }

        Object userId = session.getAttribute(SessionAuthInterceptor.SESSION_KEY);
        return userId instanceof Long ? (Long) userId : null;
    }
}
