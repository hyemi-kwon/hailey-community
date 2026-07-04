package org.example.haileyproject.comment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.haileyproject.comment.dto.CommentCreateRequest;
import org.example.haileyproject.comment.dto.CommentResponse;
import org.example.haileyproject.comment.dto.CommentUpdateRequest;
import org.example.haileyproject.common.ApiResponse;
import org.example.haileyproject.common.auth.SessionAuthInterceptor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    // 댓글 조회
    @GetMapping("/{postId}/comments")
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable Long postId) {

        return ApiResponse.success("댓글 조회 성공", commentService.getComments(postId));
    }

    // 댓글 작성 API
    @PostMapping("/{postId}/comments")
    public ApiResponse<?> createComment(@PathVariable Long postId,
                                        @RequestBody CommentCreateRequest request,
                                        HttpServletRequest httpRequest) {

        Long userId = getSessionUserId(httpRequest);

        return ApiResponse.success("댓글이 작성되었습니다.",
                commentService.createComment(postId, userId, request.getComments()));
    }

    // 댓글 수정
    @PatchMapping("/{postId}/comments/{commentId}")
    public ApiResponse<?> updateComment(@PathVariable Long postId,
                                        @PathVariable Long commentId,
                                        @RequestBody CommentUpdateRequest request,
                                        HttpServletRequest httpRequest) {

        Long userId = getSessionUserId(httpRequest);

        return ApiResponse.success("댓글이 수정되었습니다.",
                commentService.updateComment(postId, commentId, userId, request.getComments()));
    }

    //댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ApiResponse<?> deleteComment(@PathVariable Long postId,
                                        @PathVariable Long commentId) {

        commentService.deleteComment(commentId);

        return ApiResponse.success("댓글이 삭제되었습니다.", null);
    }

    private Long getSessionUserId(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute(SessionAuthInterceptor.SESSION_KEY) == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }
        return (Long) session.getAttribute(SessionAuthInterceptor.SESSION_KEY);
    }

}
