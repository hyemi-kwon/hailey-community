package org.example.haileyproject.post;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.haileyproject.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 무분별한 객체 생성 방지
@EntityListeners(AuditingEntityListener.class) //생성/수정 시간 자동화 마법
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", columnDefinition = "INT UNSIGNED")
    private Long postId;

    // @ManyToOne user_id(FK)를 연결하는 어노테이션
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩: 게시글을 조회할 때 무조건 유저 정보까지 긁어오진 않고, 필요할 때만 가져와서 성능 최적화
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 26) // 제목 최대 26글자
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false) //  TEXT 타입
    private String content;

    @Column(name = "attach_file_url")
    private String attachFileUrl;

    // 감사(Audit) 컬럼 자동화
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    // 삭제 관련 컬럼
    @Column(name = "delete_yn", nullable = false, length = 1)
    private String deleteYn = "N"; // 기본값 'N'

    // 처음 게시글을 작성할 때 사용할 안전한 생성자
    public Post(User user, String title, String content) {
        this(user, title, content, null);
    }

    public Post(User user, String title, String content, String attachFileUrl) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.attachFileUrl = attachFileUrl;

        // 작성할 때 작성자 ID(Audit)도 같이 세팅
        this.createdBy = user.getUserId();
        this.updatedBy = user.getUserId();
    }

    // 게시글 수정 전용 메서드
    public void updatePost(String title, String content) {
        updatePost(title, content, this.attachFileUrl);
    }

    public void updatePost(String title, String content, String attachFileUrl) {
        this.title = title;
        this.content = content;
        this.attachFileUrl = attachFileUrl;
    }


    // 게시글 삭제 (Soft Delete 처리)
    public void deletePost() {
        this.deleteYn = "Y";
    }
}
