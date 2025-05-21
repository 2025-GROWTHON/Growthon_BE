package com.growthon.domain.auth.service;

import com.growthon.domain.auth.dto.KakaoAuthRequest;
import com.growthon.domain.auth.dto.KakaoAuthResponse;
import com.growthon.domain.user.domain.User;
import com.growthon.domain.user.repository.UserRepository;
import com.growthon.global.jwt.JWTUtil;
import com.growthon.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public ResponseEntity<ApiResponse<KakaoAuthResponse>> kakaoAuthLogin(KakaoAuthRequest request) {
        // 1. 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    // 2. 사용자 없으면 회원가입
                    User newUser = User.builder()
                            .email(request.getEmail())
                            .username(request.getUsername())
                            .password(passwordEncoder.encode(UUID.randomUUID().toString())) // 기본 비밀번호 설정
                            .build();
                    return userRepository.save(newUser);
                });

        // 3. JWT 발급
        String token = jwtUtil.createJwt(user.getId(), user.getEmail(), user.getRole().name(), 30 * 60 * 1000L);

        // 4. 응답 객체 생성
        KakaoAuthResponse response = new KakaoAuthResponse(user.getId(), token);
        return ResponseEntity.ok(ApiResponse.of(200, "카카오 로그인 성공", response));
    }

}