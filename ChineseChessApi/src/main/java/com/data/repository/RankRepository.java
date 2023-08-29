package com.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.data.entity.Rank;

@Repository
public interface RankRepository extends JpaRepository<Rank, Integer> {

    boolean existsByName(String name);

    Optional<Rank> findByName(String name);

    boolean existsByIdNotAndName(int id, String name);

    Optional<Rank> findFirstByOrderByEloMilestonesAsc();

    boolean existsByEloMilestones(int eloMilestones);

    boolean existsByIdNotAndEloMilestones(int id, int eloMilestones);

    @Query(value = "SELECT r"
            + " FROM Rank r"
            + " WHERE r.eloMilestones <= :elo"
            + " ORDER BY r.eloMilestones DESC"
            + " LIMIT 1", nativeQuery = true)
    Optional<Rank> findByElo(@Param("elo") int elo);

    @Query("SELECT r"
            + " FROM Rank r"
            + " WHERE r.eloMilestones <= :elo"
            + " ORDER BY r.eloMilestones DESC")
    Optional<Rank> findFirstByEloMilestonesLessThanEqualOrderByEloMilestonesDesc(@Param("elo") int elo);

}
