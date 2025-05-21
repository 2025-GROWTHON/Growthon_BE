package com.growthon.domain.auth.service;

import com.growthon.domain.auth.dto.KakaoAuthRequest;
import com.growthon.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class KakaoAuthService {
    public ResponseEntity<ApiResponse<Long>> kakaoAuthLogin(KakaoAuthRequest request) {
        return ResponseEntity.ok(ApiResponse.of(201, "로그인 성공.", (long) 1));
    }
}
