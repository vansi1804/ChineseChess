package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Piece;

public interface PieceRepository 
extends JpaRepository<Piece, Integer> {

}
