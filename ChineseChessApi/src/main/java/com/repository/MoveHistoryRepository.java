package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.MoveHistory;

@Repository
public interface MoveHistoryRepository extends JpaRepository<MoveHistory, Integer>{
    
}
