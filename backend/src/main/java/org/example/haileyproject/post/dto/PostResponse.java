package org.example.haileyproject.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.example.haileyproject.post.Post;
import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private Long postId;
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Long authorId;
    private String authorNickname;
    private Long userId;
    private String nickname;
    private String profileImage;
    private String profileImageUrl;
    private String fileUrl;
    private String filePath;
    private Long commentCount;
    private Long likeCount;
    private Boolean isLiked;
    private java.util.Map<String, Object> author;

    // 값이 null이면 JSON 응답에서 이 필드 자체를 제외함
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer viewCount;

    // 1. 목록 조회용 생성자 (viewCount는 null로 유지되어 응답에서 제외됨)
    public PostResponse(Post post) {
        this.postId = post.getPostId();
        this.id = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.authorId = post.getUser().getUserId();
        this.authorNickname = post.getUser().getNickname();
        this.userId = post.getUser().getUserId();
        this.nickname = post.getUser().getNickname();
        this.profileImage = post.getUser().getProfileImage();
        this.profileImageUrl = post.getUser().getProfileImage();
        this.fileUrl = post.getAttachFileUrl();
        this.filePath = post.getAttachFileUrl();
        this.viewCount = 0;
        this.commentCount = 0L;
        this.likeCount = 0L;
        this.isLiked = false;
        this.author = new java.util.HashMap<>();
        this.author.put("userId", post.getUser().getUserId());
        this.author.put("nickname", post.getUser().getNickname());
        this.author.put("profileImageUrl", post.getUser().getProfileImage());
        this.author.put("profileImage", post.getUser().getProfileImage());
    }

    // 2. 상세 조회용 생성자 (조회수를 파라미터로 받음)
    public PostResponse(Post post, Integer viewCount) {
        this(post); // 1번 생성자 재사용
        this.viewCount = viewCount; // 상세 조회일 때만 값이 채워짐
    }

    public PostResponse(Post post, Integer viewCount, Long commentCount, Long likeCount, Boolean isLiked) {
        this(post, viewCount);
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }
}
