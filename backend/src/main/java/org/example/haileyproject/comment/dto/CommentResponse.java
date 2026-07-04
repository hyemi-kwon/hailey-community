package org.example.haileyproject.comment.dto;

import org.example.haileyproject.comment.Comment;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CommentResponse {
    private Long commentId;
    private Long id;
    private Long postId;
    private String content;
    private String createdAt;
    private Map<String, Object> author;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getCommentId();
        this.id = comment.getCommentId();
        this.postId = comment.getPost() != null ? comment.getPost().getPostId() : null;
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt() != null ? comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        
        this.author = new HashMap<>();
        if (comment.getAuthor() != null) {
            this.author.put("userId", comment.getAuthor().getUserId());
            this.author.put("nickname", comment.getAuthor().getNickname());
            this.author.put("profileImage", comment.getAuthor().getProfileImage());
            this.author.put("profileImageUrl", comment.getAuthor().getProfileImage());
        }
    }

    public Long getCommentId() {
        return commentId;
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Map<String, Object> getAuthor() {
        return author;
    }
}
