package com.growthon.domain.user.domain;

import com.growthon.domain.user.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    카카오 소셜 로그인 확장 고려
//    @Column(unique = true)
//    private String kakaoId;

    @NotBlank(message = "이름을 입력해주세요")
    @Column(nullable = false, length = 30)
    private String username;

    @NotBlank(message = "이메일 주소를 입력해주세요")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // JPA 엔티티가 DB에 저장되기 직전에 실행되는 메서드 (모든 회원은 기본적으로 USER 권한을 가짐)
    // 관리자 권한은 일반 사용자에게 자동 부여되지 않으며, 검증된 농가에게만 운영자 측에서 직접 관리자 권한을 부여
    @PrePersist
    public void prePersist() {
        if (role == null) {
            role = Role.ROLE_USER;
        }
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

}