package com.growthon.domain.user.controller;

import com.growthon.domain.user.domain.User;
import com.growthon.domain.user.dto.UserRegisterRequest;
import com.growthon.domain.user.exception.DuplicateUserException;
import com.growthon.domain.user.exception.NotFoundUserException;
import com.growthon.domain.user.repository.UserRepository;
import com.growthon.domain.user.service.UserService;
import com.growthon.global.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공")
    void registerUser_success() {
        // given
        UserRegisterRequest request = new UserRegisterRequest("진범", "jimbo@example.com", "12345678");

        // when
        User savedUser = userService.registerUser(request);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("jimbo@example.com");
        assertThat(passwordEncoder.matches("12345678", savedUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("이메일 중복으로 회원가입 실패")
    void registerUser_duplicateEmail() {
        // given
        UserRegisterRequest request1 = new UserRegisterRequest("진범", "jimbo@example.com", "1234");
        UserRegisterRequest request2 = new UserRegisterRequest("진범2", "jimbo@example.com", "5678");

        userService.registerUser(request1);

        // when & then
        assertThatThrownBy(() -> userService.registerUser(request2))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessageContaining(ErrorCode.DUPLICATE_USER.getMessage());
    }

    @Test
    @DisplayName("회원 조회 성공")
    void findUser_success() {
        // given
        UserRegisterRequest request = new UserRegisterRequest("민수", "minsu@example.com", "abcd");
        User savedUser = userService.registerUser(request);

        // when
        User found = userService.findUser(savedUser.getId());

        // then
        assertThat(found.getEmail()).isEqualTo("minsu@example.com");
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회 실패")
    void findUser_notFound() {
        // when & then
        assertThatThrownBy(() -> userService.findUser(999L))
                .isInstanceOf(NotFoundUserException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_USER.getMessage());
    }

    @Test
    @DisplayName("회원 삭제 성공")
    void deleteUser_success() {
        // given
        UserRegisterRequest request = new UserRegisterRequest("삭제맨", "delete@example.com", "pw");
        User user = userService.registerUser(request);
        Long id = user.getId();

        // when
        userService.deleteUser(id);

        // then
        assertThat(userRepository.findById(id)).isEmpty();
    }

}
