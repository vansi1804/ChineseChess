package com.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.entity.Vip;

public interface VipRepository extends JpaRepository<Vip, Integer> {
    boolean existsByName(String name);

    Optional<Vip> findByName(String name);

    boolean existsByIdNotAndName(int id, String name);
}
