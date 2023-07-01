package com.service;

import java.util.List;

import com.data.dto.RoleDTO;

public interface RoleService {
    
    List<RoleDTO> findAll();

    RoleDTO findById(int id);

    RoleDTO findByName(String name);

}
