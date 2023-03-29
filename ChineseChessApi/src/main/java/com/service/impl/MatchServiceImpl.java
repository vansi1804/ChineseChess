package com.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.ErrorMessage;
import com.data.dto.MatchCreationDTO;
import com.data.dto.MatchDTO;
import com.data.entity.Match;
import com.data.mapper.MatchMapper;
import com.data.repository.MatchRepository;
import com.data.repository.PlayerRepository;
import com.exception.ExceptionCustom;
import com.service.MatchService;
import com.service.PlayBoardService;

@Service
public class MatchServiceImpl implements MatchService {
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private MatchMapper matchMapper;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayBoardService playBoardService;

    @Override
    public List<MatchDTO> findAll() {
        return matchRepository.findAll().stream()
                .map(m -> matchMapper.toDTO(m)).collect(Collectors.toList());
    }

    @Override
    public MatchDTO findById(long id) {
        return matchRepository.findById(id)
                .map(m -> matchMapper.toDTO(m))
                .orElseThrow(() -> new ExceptionCustom("Match", ErrorMessage.DATA_NOT_FOUND, "id", id));
    }

    @Override
    public List<MatchDTO> findAllByPlayerId(long playerId) {
        return matchRepository.findAllByPlayerId(playerId).stream()
                .map(m -> matchMapper.toDTO(m))
                .collect(Collectors.toList());
    }

    @Override
    public MatchDTO create(MatchCreationDTO matchCreationDTO) {
        Match match = matchMapper.toEntity(matchCreationDTO);
        match.setPlayer1(playerRepository.findById(matchCreationDTO.getPlayer1Id())
                .orElseThrow(() -> new ExceptionCustom("Player1", ErrorMessage.DATA_NOT_FOUND, "id",
                        matchCreationDTO.getPlayer1Id())));
        match.setPlayer2(playerRepository.findById(matchCreationDTO.getPlayer2Id())
                .orElseThrow(() -> new ExceptionCustom("Player2", ErrorMessage.DATA_NOT_FOUND, "id",
                        matchCreationDTO.getPlayer2Id())));
        match.setStartAt(LocalDateTime.now());
        MatchDTO matchDTO = matchMapper.toDTO(matchRepository.save(match));
        matchDTO.setPlayBoard(playBoardService.create());
        return matchDTO;
    }

}
