package com.nvs.data.repository;

import com.nvs.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByName(String name);

    boolean existsByIdNotAndPhoneNumber(long id, String phoneNumber);
}
