package org.example.haileyproject.comment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.haileyproject.post.Post;
import org.example.haileyproject.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", columnDefinition = "INT UNSIGNED")
    private Long commentId;

    /**
     * EAGER -> 처음부터 연결을 다 읽어오는거
     * LAZY -> 필요한 시점에 연결된 데이터를 읽어오는거
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", columnDefinition = "INT UNSIGNED")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "INT UNSIGNED")
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT") //댓글내용
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt; //생성일시

    @Column(nullable = false)
    private Long createdBy; // 생성자 ID

    @UpdateTimestamp
    private LocalDateTime updatedAt; //수정일시

    @Column(nullable = false)
    private Long updatedBy; // 수정자 ID

    // 생성자, 삭제 여부 필드 등 필요시 추가
    @Column(nullable = false)
    private String deleteYn = "N";

    public Comment(Post post, User user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;

        // 작성할 때 작성자 ID(Audit)도 같이 세팅
        this.createdBy = user.getUserId();
        this.updatedBy = user.getUserId();
    }

    public void deleteComment() {
        this.deleteYn = "Y"; //삭제여부
    }

    public User getAuthor() {
        return this.user;
    }

    //댓글수정
    public void updateComment(String content, Long updatedBy) {
        this.content = content;
        this.updatedBy = updatedBy;
    }
}



