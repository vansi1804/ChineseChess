package com.data.repository;

import com.data.entity.Piece;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PieceRepository extends JpaRepository<Piece, Integer> {
  Optional<Piece> findByCurrentColAndCurrentRow(int col, int row);
}
