package com.example.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.books.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
