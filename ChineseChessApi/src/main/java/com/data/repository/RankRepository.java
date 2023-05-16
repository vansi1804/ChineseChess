package com.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.entity.Rank;

public interface RankRepository extends JpaRepository<Rank, Integer> {
    boolean existsByName(String name);

    Optional<Rank> findByName(String name);

    boolean existsByIdNotAndName(int id, String name);
}
