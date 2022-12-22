package com.team6.hanghaesisters.repository;

import com.team6.hanghaesisters.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    // findByUsername : Username을 통해 이미 생성된 사용자인지 처음 가입하는 사용자인지 판단하기 위한 메소드
}