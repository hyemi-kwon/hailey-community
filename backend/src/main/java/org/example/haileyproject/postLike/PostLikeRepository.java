package org.example.haileyproject.postLike;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.example.haileyproject.postLike.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    // 1. 조회
    Optional<PostLike> findByPost_PostIdAndUser_UserId(Long postId, Long userId);
    long countByPost_PostId(Long postId);

    // 2. 게시글 삭제 시 좋아요 정보 삭제
    void deleteByPost_PostId(Long postId);
}
