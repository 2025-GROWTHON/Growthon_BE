package com.growthon.domain.produce.controller;

import com.growthon.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 테스트용 임시 컨트롤러 클래스
@RestController
public class ProduceController {

    @GetMapping("/api/test")
    public ResponseEntity<ApiResponse<String>> test() {
        return ResponseEntity.ok(ApiResponse.of(200, "테스트 성공", "test"));
    }

}