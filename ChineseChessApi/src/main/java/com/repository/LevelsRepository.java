package com.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Levels;
@Repository
public interface LevelsRepository extends JpaRepository<Levels, Integer> {
    
}
