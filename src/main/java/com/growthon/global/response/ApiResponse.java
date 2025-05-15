package com.growthon.global.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    // API 명세 형식에 맞춰 구현한 공통 성공 응답 포맷

    private final int status;
    private final String message;
    private final T data;

    private ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 상태 코드와 메시지를 직접 지정하는 경우 (Ex. return ApiResponse.of(201, "회원가입에 성공했습니다.", savedUserDto);)
    public static <T> ApiResponse<T> of(int status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    // 단순 조회 성공 (GET)
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, "요청이 성공적으로 처리되었습니다.", data);
    }

}
