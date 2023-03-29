package com.service;

import java.util.List;

import com.data.dto.VipDTO;

public interface VipService {
    List<VipDTO> findAll();

    VipDTO findById(int id);

    VipDTO findByName(String name);

    VipDTO create(VipDTO vipDTO);

    VipDTO update(int id, VipDTO vipDTO);
    
    void delete(int id);
}
