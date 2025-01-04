package com.trickytechies.foodapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trickytechies.foodapi.model.User;

public interface UserRepository extends  JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
}
