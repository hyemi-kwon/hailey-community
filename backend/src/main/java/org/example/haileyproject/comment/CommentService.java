package org.example.haileyproject.comment;


import org.example.haileyproject.comment.dto.CommentResponse;
import org.example.haileyproject.post.Post;
import org.example.haileyproject.post.PostJpaRepository;
import org.example.haileyproject.user.User;
import org.example.haileyproject.user.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 비지니스로직_서비스클래스
@Transactional(readOnly=true) // 기본 읽기전용 트랜잭션 동작
public class CommentService {
    private final CommentRepository commentRepository; //final로 변경할 없도록 막음. 서비스클래스에서 값변경되는 상황 막을 수 있다.
    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;

    //의존성 주입 DI
    public CommentService(CommentRepository commentRepository,
                          PostJpaRepository postJpaRepository,
                          UserJpaRepository userJpaRepository) {
        this.commentRepository = commentRepository;
        this.postJpaRepository = postJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    //댓글조회
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {

        postJpaRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));


        List<Comment> commentList = commentRepository.findByPost_PostIdAndDeleteYn(postId, "N");

        return commentList.stream()
                .map(CommentResponse::new)
                .toList();
    }
    //댓글생성
    @Transactional
    public CommentResponse createComment(Long postId, Long userId, String content) {
        //게시글 찾기
        Post post=postJpaRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글입니다."));
        //유저 찾기
        User user=userJpaRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));
        //댓글객체 생성

        Comment comment = new Comment(post, user, content);

        //저장하고 반환
        return new CommentResponse(commentRepository.save(comment));
    }

    //댓글수정
    @Transactional
    public CommentResponse updateComment(Long postId, Long commentId, Long userId, String content) {

        // 1. 수정할 기존 댓글을 DB에서 찾아오기
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 2. 기존 댓글의 내용과 수정자 ID를 수정
        comment.updateComment(content, userId);

        // 3. 변경된 상태를 저장하고 결과 반환
        return new CommentResponse(commentRepository.save(comment));
    }


    //댓글삭제
    @Transactional
    public void deleteComment(Long commentId) {
        // 1. 댓글 찾기
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 2. 삭제 처리 (엔티티의 deleteYn을 'Y'로 바꿈)
        comment.deleteComment();
    }

}
