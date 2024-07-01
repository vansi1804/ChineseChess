package com.nvs.data.repository;

import com.nvs.data.entity.Vip;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VipRepository extends JpaRepository<Vip, Integer>{

   boolean existsByName(String name);

   Optional<Vip> findByName(String name);

   boolean existsByIdNotAndName(int id, String name);

   Optional<Vip> findFirstByOrderByDepositMilestonesAsc();

   Optional<Vip> findFirstByOrderByDepositMilestonesDesc();

   boolean existsByDepositMilestones(int depositMilestones);

   boolean existsByIdNotAndDepositMilestones(int id, int depositMilestones);

}
