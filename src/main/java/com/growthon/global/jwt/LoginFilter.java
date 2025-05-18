package com.growthon.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growthon.global.error.ErrorCode;
import com.growthon.global.error.ErrorResponse;
import com.growthon.global.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱용

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            // JSON 요청 파싱
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), Map.class);
            String email = credentials.get("email");
            String password = credentials.get("password");

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, password);

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse login request JSON", e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication
    ) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.iterator().hasNext() ? authorities.iterator().next().getAuthority() : "ROLE_USER";

        // JWT 생성
        String token = jwtUtil.createJwt(email, role, 60 * 60 * 1000L); // 60분 유효

        // 응답 데이터 생성
        Map<String, Object> responseData = Map.of(
                "status", 200,
                "message", "로그인에 성공했습니다.",
                "data", Map.of(
                        "accessToken", token,
                        "user", Map.of(
                                "id", userDetails.getId(),
                                "name", userDetails.getUsername(),
                                "email", userDetails.getEmail(),
                                "role", role
                        )
                )
        );

        // JSON 응답 반환
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

        try {
            // 필드 오류 생성
            ErrorResponse.FieldError fieldError = new ErrorResponse.FieldError(
                    "email or password",
                    "",
                    "Invalid email or password"
            );

            // ErrorResponse 생성
            ErrorResponse errorResponse = new ErrorResponse(
                    ErrorCode.INVALID_INPUT,
                    List.of(fieldError)
            );

            // JSON 응답 반환
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            log.error("Failed to write authentication error response", e);
        }
    }
}