package com.example.user.repository;

import com.example.user.domain.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

   // Optional<User> findByEmailAndConfirmationTokenIsNull(String email);
    Optional<User> findByEmail(String email);
}
