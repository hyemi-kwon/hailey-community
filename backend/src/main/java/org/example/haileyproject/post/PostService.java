package org.example.haileyproject.post;

import org.example.haileyproject.PostView.PostView;
import org.example.haileyproject.PostView.PostViewRepository;
import org.example.haileyproject.comment.CommentRepository;
import org.example.haileyproject.post.dto.PostCreateRequest;
import org.example.haileyproject.post.dto.PostResponse;
import org.example.haileyproject.post.dto.PostUpdateRequest;
import org.example.haileyproject.postLike.PostLikeRepository;
import org.example.haileyproject.user.User;
import org.example.haileyproject.user.UserJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostJpaRepository postRepository;
    private final UserJpaRepository userRepository;
    private final PostViewRepository postViewRepository; // 💡 추가됨
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    // 💡 생성자에 postViewRepository 추가
    public PostService(PostJpaRepository postRepository,
                       UserJpaRepository userRepository,
                       PostViewRepository postViewRepository,
                       CommentRepository commentRepository,
                       PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postViewRepository = postViewRepository;
        this.commentRepository = commentRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @Transactional
    public PostResponse createPost(PostCreateRequest request) {
        Long userId = request.getUserId();
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }
        return createPost(request, userId);
    }

    @Transactional
    public PostResponse createPost(PostCreateRequest request, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Post post = new Post(author, request.getTitle(), request.getContent(), request.getAttachFileUrl());
        Post savedPost = postRepository.save(post);
        return toResponse(savedPost, 0, userId);
    }

    public List<PostResponse> getPostList(Integer offset, Integer limit, String keyword, String sort, Long userId) {
        List<PostResponse> posts = postRepository.findAll().stream()
                .filter(post -> "N".equals(post.getDeleteYn()))
                .filter(post -> keyword == null || keyword.isBlank()
                        || post.getTitle().contains(keyword)
                        || post.getContent().contains(keyword))
                .sorted((left, right) -> right.getCreatedAt().compareTo(left.getCreatedAt()))
                .skip(offset == null ? 0 : offset)
                .limit(limit == null ? 5 : limit)
                .map(post -> toResponse(post, getViewCount(post), userId))
                .collect(Collectors.toList());

        return posts;
    }

    public Map<String, Object> getPosts(String cursor) {
        List<PostResponse> posts = getPostList(0, 100, null, null, null);

        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("nextCursor", "");
        pageInfo.put("hasNext", false);

        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);
        result.put("pageInfo", pageInfo);
        return result;
    }

    //게시글조회
    @Transactional
    public PostResponse getPost(Long postId) {
        return getPost(postId, null);
    }

    @Transactional
    public PostResponse getPost(Long postId, Long userId) {
        // 1. 게시글 찾기
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if ("Y".equals(post.getDeleteYn())) {
            throw new IllegalArgumentException("삭제된 게시글입니다.");
        }

        // 2. 조회수 찾기 (없으면 0)
        PostView postView = postViewRepository.findById(postId)
                .orElseGet(() -> {
                    PostView newView = new PostView(post);
                    return postViewRepository.save(newView);
                });

        // 3. 조회수 +1 하기
        postView.increaseView();

        // 4. 조회수를 포함해서 반환
        return toResponse(post, postView.getViewCount(), userId);
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        post.deletePost();
    }

    @Transactional
    public PostResponse updatePost(Long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        post.updatePost(
                request.getTitle() != null ? request.getTitle() : post.getTitle(),
                request.getContent() != null ? request.getContent() : post.getContent(),
                request.getAttachFileUrl() != null ? request.getAttachFileUrl() : post.getAttachFileUrl()
        );
        return toResponse(post, getViewCount(post), request.getUserId());
    }

    private PostResponse toResponse(Post post, Integer viewCount, Long userId) {
        Long commentCount = commentRepository.countByPost_PostIdAndDeleteYn(post.getPostId(), "N");
        Long likeCount = postLikeRepository.countByPost_PostId(post.getPostId());
        Boolean isLiked = userId != null
                && postLikeRepository.findByPost_PostIdAndUser_UserId(post.getPostId(), userId).isPresent();
        return new PostResponse(post, viewCount, commentCount, likeCount, isLiked);
    }

    private Integer getViewCount(Post post) {
        return postViewRepository.findById(post.getPostId())
                .map(PostView::getViewCount)
                .orElse(0);
    }
}
