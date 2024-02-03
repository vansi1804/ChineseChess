package com.service.impl;

import com.config.exception.ConflictExceptionCustomize;
import com.config.exception.ResourceNotFoundExceptionCustomize;
import com.data.dto.RankDTO;
import com.data.entity.Rank;
import com.data.mapper.RankMapper;
import com.data.repository.RankRepository;
import com.service.RankService;
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
    return rankRepository
      .findAll()
      .stream()
      .map(r -> rankMapper.toDTO(r))
      .collect(Collectors.toList());
  }

  @Override
  public RankDTO findById(int id) {
    return rankRepository
      .findById(id)
      .map(r -> rankMapper.toDTO(r))
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );
  }

  @Override
  public RankDTO findByName(String name) {
    return rankRepository
      .findByName(name)
      .map(r -> rankMapper.toDTO(r))
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("name", name)
        )
      );
  }

  @Override
  public RankDTO create(RankDTO rankDTO) {
    if (rankRepository.existsByName(rankDTO.getName())) {
      throw new ConflictExceptionCustomize(
        Collections.singletonMap("name", rankDTO.getName())
      );
    }

    if (rankRepository.existsByEloMilestones(rankDTO.getEloMilestones())) {
      throw new ConflictExceptionCustomize(
        Collections.singletonMap("eloMilestones", rankDTO.getEloMilestones())
      );
    }

    return rankMapper.toDTO(rankRepository.save(rankMapper.toEntity(rankDTO)));
  }

  @Override
  public RankDTO update(int id, RankDTO rankDTO) {
    Rank oldRank = rankRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );

    if (rankRepository.existsByIdNotAndName(id, rankDTO.getName())) {
      throw new ConflictExceptionCustomize(
        Collections.singletonMap("name", rankDTO.getName())
      );
    }

    if (
      rankRepository.existsByIdNotAndEloMilestones(
        id,
        rankDTO.getEloMilestones()
      )
    ) {
      throw new ConflictExceptionCustomize(
        Collections.singletonMap("eloMilestones", rankDTO.getEloMilestones())
      );
    }

    Rank updateRank = rankMapper.toEntity(rankDTO);
    updateRank.setId(oldRank.getId());
    
    Rank updatedRank = rankRepository.save(updateRank);
    rankRepository.flush();
    
    return rankMapper.toDTO(updatedRank);
  }

  @Override
  public boolean delete(int id) {
    rankRepository.delete(
      rankRepository
        .findById(id)
        .orElseThrow(() ->
          new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("id", id)
          )
        )
    );

    return true;
  }
}
