package org.example.haileyproject.PostView; // 기존 패키지 구조 유지

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.haileyproject.post.Post;

@Entity
@Table(name = "post_views")
@Getter
@NoArgsConstructor
public class PostView {

    @Id
    @Column(name = "post_id", columnDefinition = "INT UNSIGNED")
    private Long postId; // PK이자 FK

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // @Id와 @OneToOne 관계를 매핑
    @JoinColumn(name = "post_id", columnDefinition = "INT UNSIGNED")
    private Post post;

    @Column(nullable = false)
    private Integer viewCount = 0; // 초기값 0

    public PostView(Post post) {
        this.post = post;
        this.viewCount = 0;
    }

    // 조회수 증가 메서드
    public void increaseView() {
        this.viewCount++;
    }
}
