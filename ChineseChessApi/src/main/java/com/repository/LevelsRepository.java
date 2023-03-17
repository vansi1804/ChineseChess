package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Levels;

public interface LevelsRepository extends JpaRepository<Levels, Integer> {
    Levels findByName(String name);
}
