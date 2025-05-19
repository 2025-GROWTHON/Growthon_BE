package com.growthon.global.jwt;

import com.growthon.domain.user.domain.User;
import com.growthon.domain.user.model.Role;
import com.growthon.global.error.ErrorCode;
import com.growthon.global.jwt.exception.InvalidTokenException;
import com.growthon.global.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader(AUTH_HEADER);

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            log.debug("Authorization header is missing or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(BEARER_PREFIX.length());

        if (jwtUtil.isExpired(token)) {
            log.debug("JWT token is expired");
            throw new InvalidTokenException(ErrorCode.TOKEN_EXPIRED);
        }

        try {
            String username = jwtUtil.getUsername(token);
            String roleStr = jwtUtil.getRole(token);
            Long userId = jwtUtil.getId(token);

            Role role = Role.valueOf(roleStr);

            User user = User.builder()
                    .username(username)
                    .role(role)
                    .id(userId)
                    .build();

            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    customUserDetails,
                    null,
                    customUserDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            log.error("JWT token parsing failed", e);
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }

        filterChain.doFilter(request, response);
    }

}