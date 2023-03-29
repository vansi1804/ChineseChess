package com.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.entity.Vip;

public interface VipRepository extends JpaRepository<Vip, Integer> {
    Optional<Vip> findByName(String name);
}
