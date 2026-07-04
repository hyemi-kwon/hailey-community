package org.example.haileyproject.user;

import org.example.haileyproject.user.dto.UserInfoUpdateRequest;
import org.example.haileyproject.user.dto.UserLoginRequest;
import org.example.haileyproject.user.dto.UserPasswordUpdateRequest;
import org.example.haileyproject.user.dto.UserSignupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true) //기본적으로 데이터 조회만 하도록 설정 (속도 향상)
public class UserService {

    // 💡 스프링 데이터 JPA 인터페이스를 주입받습니다.
    private final UserJpaRepository userRepository;

    public UserService(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional // 메서드는 DB에 데이터를 '저장'해야 하므로 읽기 전용 모드를 끈다
    public Map<String, Long> signup(UserSignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("중복된 이메일 입니다.");
        }

        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임 입니다.");
        }

        User user = new User(
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                request.getProfileImage()
        );

        User savedUser = userRepository.save(user);

        Map<String, Long> data = new HashMap<>();
        data.put("userId", savedUser.getUserId());
        return data;
    }

    // 로그인은 조회만 하므로 @Transactional(readOnly = true)가 그대로 적용
    public Map<String, Object> login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호를 확인해주세요"));

        // TODO: 나중에는 비밀번호를 평문으로 비교하지 않고, 암호화(BCrypt) 비교를 해야 합니다!
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호를 확인해주세요");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getUserId());
        data.put("idx", user.getUserId());
        data.put("email", user.getEmail());
        data.put("nickname", user.getNickname());
        data.put("profileImageUrl", user.getProfileImage());
        return data;
    }

    public Map<String, Object> getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getUserId());
        data.put("idx", user.getUserId());
        data.put("email", user.getEmail());
        data.put("nickname", user.getNickname());
        data.put("profileImageUrl", user.getProfileImage());
        return data;
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void updateInfo(Long userId, UserInfoUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }


        user.updateProfile(request.getNickname(), request.getProfileImage());

        // @Transactional이 붙어있어서, 메서드가 끝나면 스프링이 알아서 UPDATE
    }

    @Transactional
    public void updateInfoWithoutPassword(Long userId, UserInfoUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        user.updateProfile(request.getNickname(), request.getProfileImage());
    }

    @Transactional
    public void updatePasswordWithoutCurrentPassword(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        user.updatePassword(password);
    }

    @Transactional
    public void updatePassword(Long userId, UserPasswordUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // Setter 대신 전용 메서드 사용
        user.updatePassword(request.getNewPassword());
    }

    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // ERD 설계에 맞춰서 데이터를 물리적으로 날리지 않고, Soft Delete(상태 변경) 처리
        user.withdraw("사용자 본인 탈퇴");
    }
}
