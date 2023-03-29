package com.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.ErrorMessage;
import com.data.dto.VipDTO;
import com.data.entity.Vip;
import com.exception.ExceptionCustom;
import com.data.mapper.VipMapper;
import com.data.repository.VipRepository;
import com.service.VipService;

@Service
public class VipServiceImpl implements VipService {
    @Autowired
    private VipRepository vipRepository;
    @Autowired
    private VipMapper vipMapper;

    @Override
    public List<VipDTO> findAll() {
        return vipRepository.findAll().stream().map(r -> vipMapper.toDTO(r)).collect(Collectors.toList());
    }

    @Override
    public VipDTO findById(int id) {
        return vipRepository.findById(id).map(r -> vipMapper.toDTO(r))
                .orElseThrow(() -> new ExceptionCustom("Vip", ErrorMessage.DATA_NOT_FOUND, "id", id));
    }

    @Override
    public VipDTO findByName(String name) {
        return vipRepository.findByName(name).map(r -> vipMapper.toDTO(r))
                .orElseThrow(() -> new ExceptionCustom("Vip", ErrorMessage.DATA_NOT_FOUND, "name", name));
    }

    @Override
    public VipDTO create(VipDTO vipDTO) {
        if (vipRepository.findByName(vipDTO.getName()).isPresent()) {
            throw new ExceptionCustom("Vip", ErrorMessage.DATA_EXISTING, "name", vipDTO.getName());
        }
        return vipMapper.toDTO(vipRepository.save(vipMapper.toEntity(vipDTO)));
    }

    @Override
    public VipDTO update(int id, VipDTO vipDTO) {
        Optional<Vip> vip = vipRepository.findByName(vipDTO.getName());
        if (vip.isPresent() && (vip.get().getId() != id)) {
            throw new ExceptionCustom("Vip", ErrorMessage.DATA_EXISTING, "name", vipDTO.getName());
        }
        return vipMapper.toDTO(vipRepository.save(vipMapper.toEntity(vipDTO)));
    }

    @Override
    public void delete(int id) {
        vipRepository.delete(vipRepository.findById(id)
                .orElseThrow(() -> new ExceptionCustom("Vip", ErrorMessage.DATA_NOT_FOUND, "id", id)));
    }

}
