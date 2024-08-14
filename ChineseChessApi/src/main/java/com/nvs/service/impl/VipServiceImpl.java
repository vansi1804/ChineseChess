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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VipServiceImpl implements VipService {

  private final VipRepository vipRepository;
  private final VipMapper vipMapper;

  @Override
  public List<VipDTO> findAll() {
    log.debug("Fetching all VIPs");
    List<VipDTO> vipDTOs = vipRepository.findAll().stream()
        .map(vipMapper::toDTO)
        .collect(Collectors.toList());
    log.debug("Found {} VIPs", vipDTOs.size());
    return vipDTOs;
  }

  @Override
  public VipDTO findById(int id) {
    log.debug("Fetching VIP by ID: {}", id);
    VipDTO vipDTO = vipRepository.findById(id)
        .map(vipMapper::toDTO)
        .orElseThrow(
            () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));
    log.debug("Found VIP: {}", vipDTO);
    return vipDTO;
  }

  @Override
  public VipDTO create(VipDTO vipDTO) {
    log.debug("Creating VIP with DTO: {}", vipDTO);

    if (vipRepository.existsByName(vipDTO.getName())) {
      throw new ConflictExceptionCustomize(Collections.singletonMap("name", vipDTO.getName()));
    }

    if (vipRepository.existsByDepositMilestones(vipDTO.getDepositMilestones())) {
      throw new ConflictExceptionCustomize(
          Collections.singletonMap("depositMilestones", vipDTO.getDepositMilestones()));
    }

    VipDTO createdVipDTO = vipMapper.toDTO(vipRepository.save(vipMapper.toEntity(vipDTO)));
    log.debug("Created VIP: {}", createdVipDTO);
    return createdVipDTO;
  }

  @Override
  public VipDTO update(int id, VipDTO vipDTO) {
    log.debug("Updating VIP with ID: {} and DTO: {}", id, vipDTO);

    Vip existingVip = vipRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));

    if (vipRepository.existsByIdNotAndName(id, vipDTO.getName())) {
      throw new ConflictExceptionCustomize(Collections.singletonMap("name", vipDTO.getName()));
    }

    if (vipRepository.existsByIdNotAndDepositMilestones(id, vipDTO.getDepositMilestones())) {
      throw new ConflictExceptionCustomize(
          Collections.singletonMap("depositMilestones", vipDTO.getDepositMilestones()));
    }

    Vip updateVip = vipMapper.toEntity(vipDTO);
    updateVip.setId(existingVip.getId());
    updateVip.setCreatedByUserId(existingVip.getCreatedByUserId());
    updateVip.setCreatedDate(existingVip.getCreatedDate());

    Vip updatedVip = vipRepository.save(updateVip);
    vipRepository.flush();

    VipDTO updatedVipDTO = vipMapper.toDTO(updatedVip);
    log.debug("Updated VIP: {}", updatedVipDTO);
    return updatedVipDTO;
  }

  @Override
  public boolean delete(int id) {
    log.debug("Deleting VIP with ID: {}", id);

    Vip vip = vipRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));
    vipRepository.delete(vip);
    log.debug("Deleted VIP with ID: {}", id);
    return true;
  }

}
