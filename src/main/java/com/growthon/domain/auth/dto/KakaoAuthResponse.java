package com.growthon.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoAuthResponse {
    private Long id;
    private String token;
}