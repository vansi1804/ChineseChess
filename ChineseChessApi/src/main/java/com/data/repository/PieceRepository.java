package com.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.entity.Piece;

public interface PieceRepository extends JpaRepository<Piece,Integer>{
    
}
