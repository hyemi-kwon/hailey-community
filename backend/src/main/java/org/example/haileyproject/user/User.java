package org.example.haileyproject.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "INT UNSIGNED")
    private Long userId;

    @Column(nullable = false, unique = true) // UNIQUE 제약 조건 추가
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10) // 최대 10글자 제약 조건 반영
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    // 감사(Audit) 컬럼 자동화 ---
    @CreatedDate
    @Column(name = "created_at", updatable = false) // 생성일시는 수정 불가
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false)
    private Long createdBy = 0L; // ERD의 Default value 반영

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy = 0L;

    //  삭제 관련 컬럼
    @Column(name = "delete_yn", nullable = false, length = 1)
    private String deleteYn = "N"; // 기본값 'N'

    @Column(name = "delete_reason")
    private String deleteReason;

    //  생성자
    public User(String email, String password, String nickname, String profileImage) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        // createdBy 등의 값은 가입 로직이나 Security Context를 통해 세팅할 수 있습니다.
    }
    // 1. 프로필 정보 수정
    public void updateProfile(String nickname, String profileImage) {
        this.nickname = nickname;
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
    }

    // 2. 비밀번호 변경
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    // 3. 회원 탈퇴 (Soft Delete: 실제 DB에서 지우지 않고 상태만 변경)
    public void withdraw(String deleteReason) {
        this.deleteYn = "Y";
        this.deleteReason = deleteReason;
    }
}