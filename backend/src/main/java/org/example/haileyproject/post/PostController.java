package org.example.haileyproject.post;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.haileyproject.common.ApiResponse;
import org.example.haileyproject.common.auth.SessionAuthInterceptor;
import org.example.haileyproject.post.dto.PostCreateRequest;
import org.example.haileyproject.post.dto.PostResponse;
import org.example.haileyproject.post.dto.PostUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Map<String, Long>> createPost(@Valid @RequestBody PostCreateRequest request, HttpServletRequest httpRequest) {
        PostResponse response = postService.createPost(request, getSessionUserId(httpRequest));
        return ApiResponse.success("생성 성공", Map.of("postId", response.getPostId()));
    }

    @GetMapping
    public ApiResponse<List<PostResponse>> getPosts(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit,
            HttpServletRequest httpRequest) {
        return ApiResponse.success("목록 조회 성공",
                postService.getPostList(offset, limit, null, null, getSessionUserIdOrNull(httpRequest)));
    }

    @GetMapping("/search")
    public ApiResponse<List<PostResponse>> searchPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String sort,
            HttpServletRequest httpRequest) {
        return ApiResponse.success("검색 성공",
                postService.getPostList(offset, limit, keyword, sort, getSessionUserIdOrNull(httpRequest)));
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId, HttpServletRequest httpRequest) {
        return ApiResponse.success("상세 조회 성공", postService.getPost(postId, getSessionUserIdOrNull(httpRequest)));
    }

    @PatchMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request,
            HttpServletRequest httpRequest) {
        request.setUserId(getSessionUserId(httpRequest));
        return ApiResponse.success("수정 성공", postService.updatePost(postId, request));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<String> deletePost(@PathVariable Long postId, HttpServletRequest httpRequest) {
        getSessionUserId(httpRequest);
        postService.deletePost(postId);
        return ApiResponse.success("삭제 성공", "삭제되었습니다.");
    }

    @PostMapping("/upload/attach-file")
    public ApiResponse<Map<String, String>> uploadAttachFile(@RequestParam("postFile") MultipartFile file) {
        String fileName = file == null ? "attach-file" : file.getOriginalFilename();
        return ApiResponse.success("첨부 파일 업로드 성공", Map.of("fileUrl", "/uploads/" + fileName));
    }

    private Long getSessionUserIdOrNull(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute(SessionAuthInterceptor.SESSION_KEY) == null) {
            return null;
        }
        return (Long) session.getAttribute(SessionAuthInterceptor.SESSION_KEY);
    }

    private Long getSessionUserId(HttpServletRequest httpRequest) {
        Long userId = getSessionUserIdOrNull(httpRequest);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }
        return userId;
    }
}
