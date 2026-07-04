package org.example.haileyproject.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<Post, Long> {
    List<Post> findByDeleteYn(String deleteYn);
}