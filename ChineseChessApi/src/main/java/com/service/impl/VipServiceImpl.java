package com.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.dto.VipDTO;
import com.config.exception.ConflictExceptionCustomize;
import com.config.exception.ResourceNotFoundExceptionCustomize;
import com.data.mapper.VipMapper;
import com.data.repository.VipRepository;
import com.service.VipService;

@Service
public class VipServiceImpl implements VipService {

    private final VipRepository vipRepository;
    private final VipMapper vipMapper;

    @Autowired
    public VipServiceImpl(
            VipRepository vipRepository,
            VipMapper vipMapper) {

        this.vipRepository = vipRepository;
        this.vipMapper = vipMapper;
    }

    @Override
    public List<VipDTO> findAll() {
        return vipRepository.findAll().stream()
                .map(r -> vipMapper.toDTO(r))
                .collect(Collectors.toList());
    }

    @Override
    public VipDTO findById(int id) {
        return vipRepository.findById(id)
                .map(r -> vipMapper.toDTO(r))
                .orElseThrow(
                        () -> new ResourceNotFoundExceptionCustomize(
                                Collections.singletonMap("id", id)));
    }

    @Override
    public VipDTO create(VipDTO vipDTO) {
        if (vipRepository.existsByName(vipDTO.getName())) {
            throw new ConflictExceptionCustomize(
                    Collections.singletonMap("name", vipDTO.getName()));
        }

        if (vipRepository.existsByDepositMilestones(vipDTO.getDepositMilestones())) {
            throw new ConflictExceptionCustomize(
                    Collections.singletonMap("depositMilestones", vipDTO.getDepositMilestones()));
        }

        return vipMapper.toDTO(vipRepository.save(vipMapper.toEntity(vipDTO)));
    }

    @Override
    public VipDTO update(int id, VipDTO vipDTO) {
        if (!vipRepository.existsById(id)) {
            throw new ResourceNotFoundExceptionCustomize(
                    Collections.singletonMap("id", id));
        }

        if (vipRepository.existsByIdNotAndName(id, vipDTO.getName())) {
            throw new ConflictExceptionCustomize(
                    Collections.singletonMap("name", vipDTO.getName()));
        }

        if (vipRepository.existsByIdNotAndDepositMilestones(id, vipDTO.getDepositMilestones())) {
            throw new ConflictExceptionCustomize(
                    Collections.singletonMap("depositMilestones", vipDTO.getDepositMilestones()));
        }

        return vipMapper.toDTO(vipRepository.save(vipMapper.toEntity(vipDTO)));
    }

    @Override
    public boolean delete(int id) {
        vipRepository.delete(vipRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundExceptionCustomize(
                                Collections.singletonMap("id", id))));

        return true;
    }

}
