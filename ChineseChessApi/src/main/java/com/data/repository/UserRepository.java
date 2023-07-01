package com.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.data.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPhoneNumber(String phoneNumber);
    
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByName(String name);

    boolean existsByIdNotAndPhoneNumber(long id, String phoneNumber);
    
}
