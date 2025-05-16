package com.growthon.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.growthon.domain.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {

    private Long id;
    private String username;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Role role;
    private String accessToken;

}