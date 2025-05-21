package com.growthon.domain.auth.controller;

import com.growthon.domain.auth.dto.KakaoAuthRequest;
import com.growthon.domain.auth.service.KakaoAuthService;
import com.growthon.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @PostMapping("/api/auth/kakao")
    public ResponseEntity<ApiResponse<Long>> kakaoAuthLogin(@RequestBody KakaoAuthRequest request) {
        return kakaoAuthService.kakaoAuthLogin(request);
    }
}
