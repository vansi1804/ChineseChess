package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Player;
public interface PlayerRepository extends JpaRepository<Player,Long>{
    
}
