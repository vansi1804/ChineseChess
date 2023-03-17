package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Game;

public interface GameRepository extends JpaRepository<Game, Integer>{
    
}
