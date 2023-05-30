package com.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.enumeration.EPiece;
import com.data.dto.PieceDTO;
import com.exception.ResourceNotFoundException;
import com.data.mapper.PieceMapper;
import com.data.repository.PieceRepository;
import com.service.PieceService;

@Service
public class PieceServiceImpl implements PieceService {
    @Autowired
    private PieceRepository pieceRepository;
    @Autowired
    private PieceMapper pieceMapper;

    @Override
    public List<PieceDTO> findAll() {
        return pieceRepository.findAll().stream().map(p -> pieceMapper.toDTO(p)).collect(Collectors.toList());
    }

    @Override
    public PieceDTO findById(int id) {
        return pieceRepository.findById(id)
                .map(p -> pieceMapper.toDTO(p))
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));
    }

    @Override
    public EPiece convertByName(String name) {
        for (EPiece piece : EPiece.values()) {
            if (piece.getValue().equalsIgnoreCase(name)) {
                return piece;
            }
        }
        throw new ResourceNotFoundException(Collections.singletonMap("name", name));
    }

}
