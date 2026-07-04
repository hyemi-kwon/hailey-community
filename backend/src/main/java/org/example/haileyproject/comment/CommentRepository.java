package org.example.haileyproject.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost_PostIdAndDeleteYn(Long postId, String deleteYn);
    long countByPost_PostIdAndDeleteYn(Long postId, String deleteYn);
}
