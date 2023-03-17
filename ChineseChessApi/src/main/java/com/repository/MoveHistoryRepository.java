package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.MoveHistory;

public interface MoveHistoryRepository extends JpaRepository<MoveHistory, Integer>{
    
}
