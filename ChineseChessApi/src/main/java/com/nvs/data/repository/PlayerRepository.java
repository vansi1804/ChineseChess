package com.nvs.data.repository;

import com.nvs.data.entity.Player;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>{

   Optional<Player> findByUser_Id(long userId);

}
