package com.service;

import com.data.dto.RoleDTO;
import java.util.List;

public interface RoleService {
  List<RoleDTO> findAll();

  RoleDTO findById(int id);

  RoleDTO findByName(String name);
}
