package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Player;
@Repository
public interface PlayerRepository extends JpaRepository<Player,Long>{
    
}
