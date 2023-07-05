package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.enumeration.EVip;
import com.data.dto.VipDTO;
import com.data.entity.Vip;
import com.config.exception.ConflictException;
import com.config.exception.ResourceNotFoundException;
import com.data.mapper.VipMapper;
import com.data.repository.VipRepository;
import com.service.VipService;

@Service
public class VipServiceImpl implements VipService {

    private final VipRepository vipRepository;
    private final VipMapper vipMapper;

    @Autowired
    public VipServiceImpl(VipRepository vipRepository, VipMapper vipMapper) {
        this.vipRepository = vipRepository;
        this.vipMapper = vipMapper;
    }

    @Override
    public List<VipDTO> findAll() {
        return vipRepository.findAll().stream().map(r -> vipMapper.toDTO(r)).collect(Collectors.toList());
    }

    @Override
    public VipDTO findById(int id) {
        return vipRepository.findById(id).map(r -> vipMapper.toDTO(r))
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));
    }

    @Override
    public VipDTO findByName(String name) {
        return vipRepository.findByName(name).map(r -> vipMapper.toDTO(r))
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("name", name)));
    }

    @Override
    public VipDTO create(VipDTO vipDTO) {
        if (vipRepository.existsByName(vipDTO.getName())) {
            throw new ResourceNotFoundException(Collections.singletonMap("name", vipDTO.getName()));
        }
        return vipMapper.toDTO(vipRepository.save(vipMapper.toEntity(vipDTO)));
    }

    @Override
    public VipDTO update(int id, VipDTO vipDTO) {
        if (!vipRepository.existsByIdNotAndName(id, vipDTO.getName())) {
            throw new ConflictException(
                    Collections.singletonMap("name", vipDTO.getName()));
        }
        return vipMapper.toDTO(vipRepository.save(vipMapper.toEntity(vipDTO)));
    }

    @Override
    public void delete(int id) {
        vipRepository.delete(vipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id))));
    }

    @PostConstruct
    public void init() {
        List<Vip> vips = new ArrayList<>();
        for (EVip eVip : EVip.values()) {
            if (!vipRepository.existsByName(eVip.name())) {
                Vip vip = new Vip();
                vip.setName(eVip.name());
                vips.add(vip);
            }
        }
        if (!vips.isEmpty()) {
            vipRepository.saveAll(vips);
        }
    }
    
}
