package com.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.data.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPhoneNumber(String phoneNumber);
    
    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.phoneNumber = :phoneNumber")
    Optional<User> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    Optional<User> findByName(String name);

    boolean existsByIdNotAndPhoneNumber(long id, String phoneNumber);
    
}
