package com.growthon.domain.user.repository;

import com.growthon.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 로그인 및 이메일 중복 체크 등에 사용
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}