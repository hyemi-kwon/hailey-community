package org.example.haileyproject.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    // 스프링이 이 이름표를 보고 알아서 "SELECT * FROM users WHERE email = ?" 쿼리를 짜줍니다!
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
