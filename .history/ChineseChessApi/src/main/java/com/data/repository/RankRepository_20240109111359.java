package com.data.repository;

import com.data.entity.Rank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepository extends JpaRepository<Rank, Integer> {
  boolean existsByName(String name);

  Optional<Rank> findByName(String name);

  boolean existsByIdNotAndName(int id, String name);

  Optional<Rank> findFirstByOrderByEloMilestonesAsc();

  boolean existsByEloMilestones(int eloMilestones);

  boolean existsByIdNotAndEloMilestones(int id, int eloMilestones);
}
