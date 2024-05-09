package com.nvs.data.repository;

import com.nvs.data.entity.Piece;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PieceRepository extends JpaRepository<Piece, Integer> {
  Optional<Piece> findByCurrentColAndCurrentRow(int col, int row);
}
