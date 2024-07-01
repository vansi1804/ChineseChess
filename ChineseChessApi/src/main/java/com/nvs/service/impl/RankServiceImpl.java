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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {

  private final RankRepository rankRepository;
  private final RankMapper rankMapper;

  @Override
  public List<RankDTO> findAll() {
    return rankRepository.findAll().stream().map(rankMapper::toDTO).collect(Collectors.toList());
  }

  @Override
  public RankDTO findById(int id) {
    return rankRepository.findById(id).map(rankMapper::toDTO).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));
  }

  @Override
  public RankDTO findByName(String name) {
    return rankRepository.findByName(name).map(rankMapper::toDTO).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("name", name)));
  }

  @Override
  public RankDTO findByPlayerElo(int elo) {
    return rankMapper.toDTO(rankRepository.findFirstByOrderByEloMilestonesAsc().orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("elo", elo))));
  }

  @Override
  public RankDTO findDefault() {
    return rankMapper.toDTO(rankRepository.findFirstByOrderByEloMilestonesAsc()
        .orElseThrow(() -> new InternalServerErrorExceptionCustomize("No rank found")));
  }

  @Override
  public RankDTO create(RankDTO rankDTO) {
    if (rankRepository.existsByName(rankDTO.getName())) {
      throw new ConflictExceptionCustomize(Collections.singletonMap("name", rankDTO.getName()));
    }

    if (rankRepository.existsByEloMilestones(rankDTO.getEloMilestones())) {
      throw new ConflictExceptionCustomize(
          Collections.singletonMap("eloMilestones", rankDTO.getEloMilestones()));
    }

    return rankMapper.toDTO(rankRepository.save(rankMapper.toEntity(rankDTO)));
  }

  @Override
  public RankDTO update(int id, RankDTO rankDTO) {
    Rank existingRank = rankRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));

    if (rankRepository.existsByIdNotAndName(id, rankDTO.getName())
        || rankRepository.existsByIdNotAndEloMilestones(id, rankDTO.getEloMilestones())) {
      throw new ConflictExceptionCustomize(
          Collections.singletonMap("conflict", "Name or eloMilestones conflict"));
    }

    Rank updateRank = rankMapper.toEntity(rankDTO);
    updateRank.setId(id);
    updateRank.setCreatedByUserId(existingRank.getCreatedByUserId());
    updateRank.setCreatedDate(existingRank.getCreatedDate());

    Rank updatedRank = rankRepository.save(updateRank);
    rankRepository.flush();

    return rankMapper.toDTO(updatedRank);
  }

  @Override
  public boolean delete(int id) {
    rankRepository.delete(rankRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id))));
    return true;
  }

}
