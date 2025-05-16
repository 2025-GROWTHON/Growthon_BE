package com.growthon.domain.user.service;

import com.growthon.domain.user.domain.User;
import com.growthon.domain.user.dto.UserRegisterRequest;
import com.growthon.domain.user.exception.DuplicateUserException;
import com.growthon.domain.user.exception.NotFoundUserException;
import com.growthon.domain.user.repository.UserRepository;
import com.growthon.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException(ErrorCode.DUPLICATE_USER);
        }

        // 비밀번호 암호화
        User user = request.toEntity();
        user.encodePassword(passwordEncoder); // User 엔티티에 메서드를 만들면 더 깔끔

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findUser(id);
        userRepository.delete(user);
    }

}