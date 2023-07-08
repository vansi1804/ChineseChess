package com.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.data.entity.Vip;

@Repository
public interface VipRepository extends JpaRepository<Vip, Integer> {

    boolean existsByName(String name);

    Optional<Vip> findByName(String name);

    boolean existsByIdNotAndName(int id, String name);

    Optional<Vip> findFirstByOrderByDepositMilestonesAsc();

    Optional<Vip> findFirstByOrderByDepositMilestonesDesc();
    
}
