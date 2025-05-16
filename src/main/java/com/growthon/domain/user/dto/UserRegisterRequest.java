package com.growthon.domain.user.dto;

import com.growthon.domain.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegisterRequest {

    @NotBlank(message = "이름을 입력해주세요")
    private String username;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    public User toEntity() {
        return User
                .builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
    }

}
