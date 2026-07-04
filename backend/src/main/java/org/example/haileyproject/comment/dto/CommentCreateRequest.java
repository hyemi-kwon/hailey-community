package org.example.haileyproject.comment.dto;

import lombok.Getter;

@Getter
public class CommentCreateRequest {

    private Long userId;
    private String comments;
    private String commentContent;

    public String getComments() {
        return comments != null ? comments : commentContent;
    }
}
