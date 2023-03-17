package com.service;

import java.util.List;

import com.dto.RoleDTO;

public interface RoleService {
    List<RoleDTO> findAll();

    RoleDTO findById(int id);

    RoleDTO create(RoleDTO roleDTO);

    RoleDTO update(int id, RoleDTO roleDTO);

    void delete(int id);
}
