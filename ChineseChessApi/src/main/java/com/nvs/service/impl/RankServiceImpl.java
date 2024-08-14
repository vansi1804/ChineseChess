package com.nvs.service.impl;

import com.nvs.config.exception.ConflictExceptionCustomize;
import com.nvs.config.exception.InternalServerErrorExceptionCustomize;
import com.nvs.config.exception.ResourceNotFoundExceptionCustomize;
import com.nvs.data.dto.RankDTO;
import com.nvs.data.entity.Rank;
import com.nvs.data.mapper.RankMapper;
import com.nvs.data.repository.RankRepository;
import com.nvs.service.RankService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankServiceImpl implements RankService {

  private final RankRepository rankRepository;
  private final RankMapper rankMapper;

  @Cacheable(value = "ranks")
  @Override
  public List<RankDTO> findAll() {
    log.debug("Finding all ranks");
    List<RankDTO> ranks = rankRepository.findAll().stream().map(rankMapper::toDTO)
        .collect(Collectors.toList());
    log.debug("Found {} ranks", ranks.size());
    return ranks;
  }

  @Cacheable(value = "rank", key = "#id")
  @Override
  public RankDTO findById(int id) {
    log.debug("Finding rank by ID: {}", id);
    RankDTO rankDTO = rankRepository.findById(id).map(rankMapper::toDTO).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));
    log.debug("Found rank: {}", rankDTO);
    return rankDTO;
  }

  @Cacheable(value = "rank", key = "#name")
  @Override
  public RankDTO findByName(String name) {
    log.debug("Finding rank by name: {}", name);
    RankDTO rankDTO = rankRepository.findByName(name).map(rankMapper::toDTO).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("name", name)));
    log.debug("Found rank: {}", rankDTO);
    return rankDTO;
  }

  @Cacheable(value = "rank", key = "#elo")
  @Override
  public RankDTO findByPlayerElo(int elo) {
    log.debug("Finding rank by player Elo: {}", elo);
    RankDTO rankDTO = rankMapper.toDTO(
        rankRepository.findFirstByOrderByEloMilestonesAsc().orElseThrow(
            () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("elo", elo))));
    log.debug("Found rank: {}", rankDTO);
    return rankDTO;
  }

  @Cacheable(value = "defaultRank")
  @Override
  public RankDTO findDefault() {
    log.debug("Finding default rank");
    RankDTO rankDTO = rankMapper.toDTO(rankRepository.findFirstByOrderByEloMilestonesAsc()
        .orElseThrow(() -> new InternalServerErrorExceptionCustomize("No rank found")));
    log.debug("Found default rank: {}", rankDTO);
    return rankDTO;
  }

  @CachePut(value = "rank", key = "#rankDTO.name")
  @Override
  public RankDTO create(RankDTO rankDTO) {
    log.debug("Creating rank with details: {}", rankDTO);

    if (rankRepository.existsByName(rankDTO.getName())) {
      log.warn("Rank creation failed due to name conflict: {}", rankDTO.getName());
      throw new ConflictExceptionCustomize(Collections.singletonMap("name", rankDTO.getName()));
    }

    if (rankRepository.existsByEloMilestones(rankDTO.getEloMilestones())) {
      log.warn("Rank creation failed due to Elo milestones conflict: {}",
          rankDTO.getEloMilestones());
      throw new ConflictExceptionCustomize(
          Collections.singletonMap("eloMilestones", rankDTO.getEloMilestones()));
    }

    RankDTO createdRankDTO = rankMapper.toDTO(rankRepository.save(rankMapper.toEntity(rankDTO)));
    log.debug("Rank created successfully: {}", createdRankDTO);
    return createdRankDTO;
  }

  @CachePut(value = "rank", key = "#id")
  @Override
  public RankDTO update(int id, RankDTO rankDTO) {
    log.debug("Updating rank with ID: {} and details: {}", id, rankDTO);

    Rank existingRank = rankRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));

    if (rankRepository.existsByIdNotAndName(id, rankDTO.getName())
        || rankRepository.existsByIdNotAndEloMilestones(id, rankDTO.getEloMilestones())) {
      log.warn("Rank update failed due to name or Elo milestones conflict");
      throw new ConflictExceptionCustomize(
          Collections.singletonMap("conflict", "Name or eloMilestones conflict"));
    }

    Rank updateRank = rankMapper.toEntity(rankDTO);
    updateRank.setId(id);
    updateRank.setCreatedByUserId(existingRank.getCreatedByUserId());
    updateRank.setCreatedDate(existingRank.getCreatedDate());

    Rank updatedRank = rankRepository.save(updateRank);
    rankRepository.flush();

    RankDTO updatedRankDTO = rankMapper.toDTO(updatedRank);
    log.debug("Rank updated successfully: {}", updatedRankDTO);
    return updatedRankDTO;
  }

  @CacheEvict(value = "rank", key = "#id")
  @Override
  public boolean delete(int id) {
    log.debug("Deleting rank with ID: {}", id);
    rankRepository.delete(rankRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id))));
    log.debug("Rank with ID: {} deleted successfully", id);
    return true;
  }
}
