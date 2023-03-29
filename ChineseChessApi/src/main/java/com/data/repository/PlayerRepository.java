package com.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.data.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByUserId(long userId);

    @Query("SELECT p FROM Player p WHERE p.user.phoneNumber = :phoneNumber")
    Optional<Player> findByPhoneNumber(@Param(value = "phoneNumber") String phoneNumber);

    List<Player> findAllByLevelsId(int levelsId);

    List<Player> findAllByEloBetween(int fromElo, int toElo);

}
