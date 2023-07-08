package com.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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
        return rankRepository.findAll().stream().map(r -> rankMapper.toDTO(r)).collect(Collectors.toList());
    }

    @Override
    public RankDTO findById(int id) {
        return rankRepository.findById(id)
                .map(r -> rankMapper.toDTO(r))
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));
    }

    @Override
    public RankDTO findByName(String name) {
        return rankRepository.findByName(name)
                .map(r -> rankMapper.toDTO(r))
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("name", name)));
    }

    @Override
    public RankDTO create(RankDTO rankDTO) {
        if (rankRepository.existsByName(rankDTO.getName())) {
            throw new ResourceNotFoundException(Collections.singletonMap("name", rankDTO.getName()));
        }
        return rankMapper.toDTO(rankRepository.save(rankMapper.toEntity(rankDTO)));
    }

    @Override
    public RankDTO update(int id, RankDTO rankDTO) {
        if (!rankRepository.existsByIdNotAndName(id, rankDTO.getName())) {
            throw new ConflictException(
                    Collections.singletonMap("name", rankDTO.getName()));
        }
        return rankMapper.toDTO(rankRepository.save(rankMapper.toEntity(rankDTO)));
    }

    @Override
    public void delete(int id) {
        rankRepository.delete(rankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id))));
    }

    @PostConstruct
    public void init() {
        if (rankRepository.count() == 0) {
            rankRepository.save(new Rank(1, "Novice", 2000));
        }
    }

}
