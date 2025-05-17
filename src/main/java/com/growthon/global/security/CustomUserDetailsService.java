package com.growthon.global.security;

import com.growthon.domain.user.domain.User;
import com.growthon.domain.user.exception.NotFoundUserException;
import com.growthon.domain.user.repository.UserRepository;
import com.growthon.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 이메일로 사용자 조회
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));

        return new CustomUserDetails(user);
    }

}