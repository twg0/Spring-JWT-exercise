package com.example.springjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springjwt.domain.Auth;
import com.example.springjwt.domain.User;

@Repository
public interface AuthRepository extends JpaRepository<Auth,Long> {
	Optional<Auth> findByRefreshToken(String refreshToken);
	Boolean existsByUser(User user);
}
