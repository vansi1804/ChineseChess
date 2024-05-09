package com.nvs.data.repository;

import com.nvs.data.entity.Player;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
  Optional<Player> findByUserId(long userId);

  Optional<Player> findByUser_PhoneNumber(String phoneNumber);

  List<Player> findAllByEloBetween(int fromElo, int toElo);

  Optional<Player> findByUser_Id(long userId);
}
