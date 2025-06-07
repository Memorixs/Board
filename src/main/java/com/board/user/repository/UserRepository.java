package com.board.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
}
