package org.example.haileyproject.post.dto;

public class PostUpdateRequest {
    private Long userId;
    private String title;
    private String content;
    private String image;
    private String attachFileUrl;

    public PostUpdateRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAttachFileUrl() {
        return attachFileUrl != null ? attachFileUrl : image;
    }

    public void setAttachFileUrl(String attachFileUrl) {
        this.attachFileUrl = attachFileUrl;
    }
}
