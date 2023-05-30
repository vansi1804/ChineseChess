package com.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.data.entity.Piece;

public interface PieceRepository extends JpaRepository<Piece,Integer>{
    
    Optional<Piece> findByCurrentColAndCurrentRow(int colMovingTo, int rowMovingTo);
    
}
