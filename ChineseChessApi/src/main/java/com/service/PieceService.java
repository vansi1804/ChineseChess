package com.service;

import java.util.List;

import com.data.dto.PieceDTO;

public interface PieceService {
    List<PieceDTO> findAll();

    PieceDTO findById(int id);
}
