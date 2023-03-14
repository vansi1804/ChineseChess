package com.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dto.PieceDTO;
import com.mapper.PieceMapper;
import com.repository.PieceRepository;
import com.service.PieceService;

@Service
public class PieceServiceImpl implements PieceService{

    @Autowired
    private PieceRepository pieceRepository;

    @Autowired
    private PieceMapper pieceMapper;

    @Override
    public List<PieceDTO> findAll() {
        return pieceRepository.findAll().stream().map(p->pieceMapper.toDTO(p)).collect(Collectors.toList());
    }
   
}
