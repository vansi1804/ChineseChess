package com.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsByName(String name);

    Optional<Role> findByName(String name);
}
