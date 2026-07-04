package org.example.haileyproject.postLike;

import org.example.haileyproject.post.Post;
import org.example.haileyproject.post.PostJpaRepository;
import org.example.haileyproject.user.User;
import org.example.haileyproject.user.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 👈 이게 핵심!

import java.util.Optional;


@Service
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public PostLikeService(PostLikeRepository postLikeRepository,
                           PostJpaRepository postJpaRepository,
                           UserJpaRepository userJpaRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postJpaRepository = postJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Transactional
    public String toggleLike(Long postId, Long userId) {
        Optional<PostLike> existingLike = postLikeRepository.findByPost_PostIdAndUser_UserId(postId, userId);

        if (existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
            return "좋아요 취소!";
        } else {
            Post post = postJpaRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
            User user = userJpaRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

            postLikeRepository.save(new PostLike(post, user));
            return "좋아요 성공!";
        }
    }

    @Transactional
    public Long like(Long postId, Long userId) {
        Optional<PostLike> existingLike = postLikeRepository.findByPost_PostIdAndUser_UserId(postId, userId);
        if (existingLike.isEmpty()) {
            Post post = postJpaRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
            User user = userJpaRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
            postLikeRepository.save(new PostLike(post, user));
        }
        return postLikeRepository.countByPost_PostId(postId);
    }

    @Transactional
    public Long unlike(Long postId, Long userId) {
        postLikeRepository.findByPost_PostIdAndUser_UserId(postId, userId)
                .ifPresent(postLikeRepository::delete);
        return postLikeRepository.countByPost_PostId(postId);
    }
}
