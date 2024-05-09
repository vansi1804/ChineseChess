package com.nvs.service.impl;

import com.nvs.config.exception.ConflictExceptionCustomize;
import com.nvs.config.exception.ResourceNotFoundExceptionCustomize;
import com.nvs.data.dto.VipDTO;
import com.nvs.data.entity.Vip;
import com.nvs.data.mapper.VipMapper;
import com.nvs.data.repository.VipRepository;
import com.nvs.service.VipService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VipServiceImpl implements VipService {

  private final VipRepository vipRepository;
  private final VipMapper vipMapper;

  @Override
  public List<VipDTO> findAll() {
    return vipRepository
        .findAll()
        .stream()
        .map(r -> vipMapper.toDTO(r))
        .collect(Collectors.toList());
  }

  @Override
  public VipDTO findById(int id) {
    return vipRepository
        .findById(id)
        .map(r -> vipMapper.toDTO(r))
        .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
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
          Collections.singletonMap(
              "depositMilestones",
              vipDTO.getDepositMilestones()));
    }

    return vipMapper.toDTO(vipRepository.save(vipMapper.toEntity(vipDTO)));
  }

  @Override
  public VipDTO update(int id, VipDTO vipDTO) {
    Vip existingVip = vipRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("id", id)));

    if (vipRepository.existsByIdNotAndName(id, vipDTO.getName())) {
      throw new ConflictExceptionCustomize(
          Collections.singletonMap("name", vipDTO.getName()));
    }

    if (vipRepository.existsByIdNotAndDepositMilestones(
        id,
        vipDTO.getDepositMilestones())) {
      throw new ConflictExceptionCustomize(
          Collections.singletonMap(
              "depositMilestones",
              vipDTO.getDepositMilestones()));
    }

    Vip updateVip = vipMapper.toEntity(vipDTO);
    updateVip.setId(existingVip.getId());
    updateVip.setCreatedByUserId(existingVip.getCreatedByUserId());
    updateVip.setCreatedDate(existingVip.getCreatedDate());

    Vip updatedVip = vipRepository.save(updateVip);
    vipRepository.flush();

    return vipMapper.toDTO(updatedVip);
  }

  @Override
  public boolean delete(int id) {
    vipRepository.delete(
        vipRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
                Collections.singletonMap("id", id))));

    return true;
  }
}
