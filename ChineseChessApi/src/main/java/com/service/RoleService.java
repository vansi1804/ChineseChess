package com.service;

import java.util.List;

import com.data.dto.RoleDTO;

public interface RoleService {
    List<RoleDTO> findAll();

    RoleDTO findById(int id);

    RoleDTO findByName(String name);

    RoleDTO create(RoleDTO roleDTO);

    RoleDTO update(int id, RoleDTO roleDTO);
    
    void delete(int id);
}
