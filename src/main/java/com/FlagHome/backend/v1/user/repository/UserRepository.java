package com.FlagHome.backend.v1.user.repository;

import com.FlagHome.backend.v1.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByusername(String username);

    Optional<User> findByUserId(long userId);
}
