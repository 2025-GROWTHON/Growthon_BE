package com.growthon.global.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // 공통
    INTERNAL_SERVER_ERROR("서버에 문제가 발생했습니다.", 500),
    INVALID_INPUT("입력값이 유효하지 않습니다.", 400),

    // 인증 & 권한
    UNAUTHORIZED("로그인이 필요합니다.", 401),
    FORBIDDEN("접근 권한이 없습니다.", 403),

    // 비즈니스
    NOT_FOUND_PRODUCE("해당 농산물을 찾을 수 없습니다.", 404),
    NOT_FOUND_USER("존재하지 않는 회원입니다.", 404),
    DUPLICATE_USER("이미 존재하는 회원입니다.", 409),
    ACCESS_DENIED("상품을 수정할 권한이 없습니다.", 403),
    NO_IMAGE_FILE("업로드된 이미지 파일이 없습니다.", 400),

    // Token
    INVALID_TOKEN("유효하지 않은 토큰입니다.", 401),
    TOKEN_EXPIRED("토큰이 만료되었습니다.", 401);

    private final String message;
    private final int status;
}
