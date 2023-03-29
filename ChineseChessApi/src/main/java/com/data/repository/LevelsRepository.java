package com.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.entity.Levels;

public interface LevelsRepository extends JpaRepository<Levels, Integer> {
    Optional<Levels> findByName(String name);
}
