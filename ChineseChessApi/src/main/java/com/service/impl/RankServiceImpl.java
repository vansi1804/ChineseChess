package com.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.dto.RankDTO;
import com.data.entity.Rank;
import com.config.exception.ConflictException;
import com.config.exception.ResourceNotFoundException;
import com.data.mapper.RankMapper;
import com.data.repository.RankRepository;
import com.service.RankService;

@Service
public class RankServiceImpl implements RankService {

    private final RankRepository rankRepository;
    private final RankMapper rankMapper;

    @Autowired
    public RankServiceImpl(RankRepository rankRepository, RankMapper rankMapper) {
        this.rankRepository = rankRepository;
        this.rankMapper = rankMapper;
    }

    @Override
    public List<RankDTO> findAll() {
        return rankRepository.findAll().stream()
                .map(r -> rankMapper.toDTO(r))
                .collect(Collectors.toList());
    }

    @Override
    public RankDTO findById(int id) {
        return rankRepository.findById(id)
                .map(r -> rankMapper.toDTO(r))
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("id", id)));
    }

    @Override
    public RankDTO findByName(String name) {
        return rankRepository.findByName(name)
                .map(r -> rankMapper.toDTO(r))
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("name", name)));
    }

    @Override
    public RankDTO create(RankDTO rankDTO) {
        if (rankRepository.existsByName(rankDTO.getName())) {
            throw new ConflictException(Collections.singletonMap("name", rankDTO.getName()));
        }
        
        if (rankRepository.existsByEloMilestones(rankDTO.getEloMilestones())) {
            throw new ConflictException(Collections.singletonMap("eloMilestones", rankDTO.getEloMilestones()));
        }

        return rankMapper.toDTO(rankRepository.save(rankMapper.toEntity(rankDTO)));
    }

    @Override
    public RankDTO update(int id, RankDTO rankDTO) {
        if (!rankRepository.existsById(id)) {
            throw new ResourceNotFoundException(Collections.singletonMap("id", id));
        }

        if (rankRepository.existsByIdNotAndName(id, rankDTO.getName())) {
            throw new ConflictException(Collections.singletonMap("name", rankDTO.getName()));
        }

        if (rankRepository.existsByIdNotAndEloMilestones(id, rankDTO.getEloMilestones())) {
            throw new ConflictException(Collections.singletonMap("eloMilestones", rankDTO.getEloMilestones()));
        }

        Rank updateRank = rankMapper.toEntity(rankDTO);
        updateRank.setId(id);

        return rankMapper.toDTO(rankRepository.save(updateRank));
    }

    @Override
    public boolean delete(int id) {
        rankRepository.delete(rankRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("id", id))));

        return true;
    }

}
