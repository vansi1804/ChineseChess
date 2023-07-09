package com.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.data.entity.Rank;

@Repository
public interface RankRepository extends JpaRepository<Rank, Integer> {
    
    boolean existsByName(String name);

    Optional<Rank> findByName(String name);

    boolean existsByIdNotAndName(int id, String name);

    Optional<Rank> findFirstByOrderByEloMilestonesAsc();

    boolean existsByEloMilestones(Integer eloMilestones);

    boolean existsByIdNotAndEloMilestones(int id, Integer eloMilestones);

}
