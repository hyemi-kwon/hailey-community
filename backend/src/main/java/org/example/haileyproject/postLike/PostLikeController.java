package org.example.haileyproject.postLike;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.haileyproject.common.ApiResponse;
import org.example.haileyproject.common.auth.SessionAuthInterceptor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    // 좋아요
    @PostMapping("/{postId}/likes")
    public ApiResponse<?> like(@PathVariable Long postId, HttpServletRequest httpRequest) {
        Long userId = getSessionUserId(httpRequest);

        Long likeCount = postLikeService.like(postId, userId);

        return ApiResponse.success("좋아요 성공", Map.of("likeCount", likeCount));
    }

    @DeleteMapping("/{postId}/likes")
    public ApiResponse<?> unlike(@PathVariable Long postId, HttpServletRequest httpRequest) {
        Long userId = getSessionUserId(httpRequest);

        Long likeCount = postLikeService.unlike(postId, userId);

        return ApiResponse.success("좋아요 취소 성공", Map.of("likeCount", likeCount));
    }

    private Long getSessionUserId(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute(SessionAuthInterceptor.SESSION_KEY) == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }
        return (Long) session.getAttribute(SessionAuthInterceptor.SESSION_KEY);
    }
}
