package com.nvs.service;

import com.nvs.data.dto.VipDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface VipService{

   List<VipDTO> findAll();

   VipDTO findById(int id);

   VipDTO create(VipDTO vipDTO);

   VipDTO update(int id, VipDTO vipDTO);

   @Transactional
   boolean delete(int id);

}
