package com.growthon.domain.user.controller;

import com.growthon.domain.user.domain.User;
import com.growthon.domain.user.dto.UserLoginRequest;
import com.growthon.domain.user.dto.UserLoginResponse;
import com.growthon.domain.user.dto.UserRegisterRequest;
import com.growthon.domain.user.service.UserService;
import com.growthon.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> createUser(@Valid @RequestBody UserRegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.of(201, "회원가입이 완료되었습니다.", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> findUser(@PathVariable Long id) {
        User user = userService.findUser(id);
        return ResponseEntity.ok(ApiResponse.ok(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.of(200, "회원 탈퇴가 완료되었습니다.", null));
    }

    // 로그인 (JWT 구현 후 작성)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(@Valid @RequestBody UserLoginRequest request) {
        // 추후 AuthService로 위임
        return ResponseEntity.ok().build(); // 임시
    }

}
