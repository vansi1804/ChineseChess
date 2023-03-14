package com.service;

import java.util.List;

import com.dto.RoleDTO;

public interface RoleService {
    List<RoleDTO> findAll();
    RoleDTO findByName(String name);
    RoleDTO create(RoleDTO roleDTO);
    RoleDTO update(String lastName, RoleDTO roleDTO);
}
