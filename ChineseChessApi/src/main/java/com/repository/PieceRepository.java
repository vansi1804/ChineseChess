package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Piece;

@Repository
public interface PieceRepository 
extends JpaRepository<Piece, Integer> {

}
