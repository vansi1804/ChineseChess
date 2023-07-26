package com.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.ErrorMessage;
import com.common.enumeration.EResult;
import com.data.dto.match.MatchDetailDTO;
import com.data.dto.match.MatchCreationDTO;
import com.data.dto.match.MatchDTO;
import com.data.dto.move.MoveHistoryDTO;
import com.data.dto.player.PlayerProfileDTO;
import com.data.entity.Match;
import com.data.entity.MoveHistory;
import com.data.entity.Player;
import com.data.mapper.MatchMapper;
import com.data.repository.MatchRepository;
import com.data.repository.MoveHistoryRepository;
import com.data.repository.PlayerRepository;
import com.config.exception.InvalidException;
import com.config.exception.ResourceNotFoundException;
import com.service.MatchService;
import com.service.MoveService;
import com.service.PlayerService;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;
    private final MoveHistoryRepository moveHistoryRepository;
    private final MoveService moveService;

    @Autowired
    public MatchServiceImpl(
            MatchRepository matchRepository,
            MatchMapper matchMapper,
            PlayerRepository playerRepository,
            PlayerService playerService,
            MoveHistoryRepository moveHistoryRepository,
            MoveService moveService) {

        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
        this.playerRepository = playerRepository;
        this.playerService = playerService;
        this.moveHistoryRepository = moveHistoryRepository;
        this.moveService = moveService;
    }

    @Override
    public List<MatchDTO> findAll() {
        return matchRepository.findAll().stream()
                .map(m -> matchMapper.toDTO(m))
                .collect(Collectors.toList());
    }

    @Override
    public MatchDTO findById(long id) {
        return matchRepository.findById(id)
                .map(m -> matchMapper.toDTO(m))
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("id", id)));
    }

    @Override
    public List<MatchDTO> findAllByPlayerId(long playerId) {
        return matchRepository.findAllByPlayerId(playerId).stream()
                .map(m -> matchMapper.toDTO(m))
                .collect(Collectors.toList());
    }

    @Override
    public MatchDetailDTO findDetailById(long id) {
        MatchDetailDTO matchDetailDTO = matchRepository.findById(id)
                .map(m -> matchMapper.toDetailDTO(m))
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("id", id)));

        List<MoveHistory> moveHistories = moveHistoryRepository.findAllByMatch_Id(id);
        Map<Long, MoveHistoryDTO> moveHistoryDTOs = moveService.build(moveHistories);

        matchDetailDTO.setTotalTurn((long) moveHistoryDTOs.size());
        matchDetailDTO.setMoveHistoryDTOs(moveHistoryDTOs);

        return matchDetailDTO;
    }

    @Override
    public MatchDTO create(MatchCreationDTO matchCreationDTO) {
        Player player1 = playerRepository.findById(matchCreationDTO.getPlayer1Id())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("player1Id", matchCreationDTO.getPlayer1Id())));

        Player player2 = playerRepository.findById(matchCreationDTO.getPlayer2Id())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("player2Id", matchCreationDTO.getPlayer2Id())));

        if (matchRepository.existsPlayingByPlayerId(player1.getId())) {
            throw new InvalidException(
                    ErrorMessage.PLAYER_PLAYING,
                    Collections.singletonMap("player1Id", player1.getId()));
        }

        if (matchRepository.existsPlayingByPlayerId(player2.getId())) {
            throw new InvalidException(
                    ErrorMessage.PLAYER_PLAYING,
                    Collections.singletonMap("player2Id", player2.getId()));
        }

        if (player1.getElo() < matchCreationDTO.getEloBet()) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("player1Id", player1.getId());
            errors.put("elo", player1.getElo());
            errors.put("eloBet", player1.getElo());
            throw new InvalidException(ErrorMessage.PLAYER_PLAYING, errors);
        }

        if (player2.getElo() < matchCreationDTO.getEloBet()) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("player2Id", player2.getId());
            errors.put("elo", player2.getElo());
            errors.put("eloBet", player2.getElo());
            throw new InvalidException(ErrorMessage.PLAYER_PLAYING, errors);
        }

        return matchMapper.toDTO(matchRepository.save(matchMapper.toEntity(matchCreationDTO)));
    }

    @Override
    public MatchDTO updateResult(long id, Boolean isRedWin) {
        Match match = matchRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("id", id)));

        if (match.getResult() != null) {
            throw new InvalidException(ErrorMessage.END_MATCH, Collections.singletonMap("id", match.getId()));
        }

        EResult eResult;
        if (isRedWin == null) {
            match.setResult(EResult.DRAW.getValue());
            eResult = EResult.DRAW;
        } else if (isRedWin) {
            match.setResult(EResult.WIN.getValue());
            eResult = EResult.WIN;
        } else {
            match.setResult(EResult.LOSE.getValue());
            eResult = EResult.LOSE;
        }

        MatchDTO matchDTO = matchMapper.toDTO(matchRepository.save(match));

        PlayerProfileDTO player1ProfileDTO = playerService.updateByEloBetAndResult(
                match.getPlayer1().getId(), match.getEloBet(), eResult);
        matchDTO.setPlayer1ProfileDTO(player1ProfileDTO);

        PlayerProfileDTO player2ProfileDTO = playerService.updateByEloBetAndResult(
                match.getPlayer2().getId(), match.getEloBet(), eResult);
        matchDTO.setPlayer2ProfileDTO(player2ProfileDTO);

        return matchDTO;
    }

}
