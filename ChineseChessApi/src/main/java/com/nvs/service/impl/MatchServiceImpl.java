package com.nvs.service.impl;

import com.nvs.common.ErrorMessage;
import com.nvs.common.enumeration.EMatchResult;
import com.nvs.config.exception.InvalidExceptionCustomize;
import com.nvs.config.exception.ResourceNotFoundExceptionCustomize;
import com.nvs.data.dto.match.MatchCreationDTO;
import com.nvs.data.dto.match.MatchDTO;
import com.nvs.data.dto.match.MatchDetailDTO;
import com.nvs.data.dto.move.MoveHistoryDTO;
import com.nvs.data.dto.player.PlayerProfileDTO;
import com.nvs.data.entity.Match;
import com.nvs.data.entity.Player;
import com.nvs.data.mapper.MatchMapper;
import com.nvs.data.repository.MatchRepository;
import com.nvs.data.repository.PlayerRepository;
import com.nvs.service.MatchService;
import com.nvs.service.MoveService;
import com.nvs.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;
    private final MoveService moveService;

    @Override
    public List<MatchDTO> findAll() {
        return matchRepository
                .findAll()
                .stream()
                .map(matchMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MatchDTO findById(long id) {
        return matchRepository
                .findById(id)
                .map(matchMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
                        Collections.singletonMap("id", id)));
    }

    @Override
    public List<MatchDTO> findAllByPlayerId(long playerId) {
        return matchRepository
                .findAllByPlayerId(playerId)
                .stream()
                .map(matchMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MatchDetailDTO findDetailById(long id) {
        MatchDetailDTO matchDetailDTO = matchRepository
                .findById(id)
                .map(matchMapper::toDetailDTO)
                .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
                        Collections.singletonMap("id", id)));

        Map<Long, MoveHistoryDTO> moveHistoryDTOs = moveService.findAllByMatchId(
                id);

        matchDetailDTO.setTotalTurn((long) moveHistoryDTOs.size());
        matchDetailDTO.setMoveHistoryDTOs(moveHistoryDTOs);

        return matchDetailDTO;
    }

    @Override
    public MatchDTO create(MatchCreationDTO matchCreationDTO) {
        Player player1 = playerRepository
                .findById(matchCreationDTO.getPlayer1Id())
                .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
                        Collections.singletonMap("player1Id", matchCreationDTO.getPlayer1Id())));

        Player player2 = playerRepository
                .findById(matchCreationDTO.getPlayer2Id())
                .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
                        Collections.singletonMap("player2Id", matchCreationDTO.getPlayer2Id())));

        if (matchRepository.existsPlayingByPlayerId(player1.getId())) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("message", ErrorMessage.PLAYER_PLAYING);
            errors.put("player1Id", player1.getId());

            throw new InvalidExceptionCustomize(errors);
        }

        if (matchRepository.existsPlayingByPlayerId(player2.getId())) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("message", ErrorMessage.PLAYER_PLAYING);
            errors.put("player2Id", player2.getId());

            throw new InvalidExceptionCustomize(errors);
        }

        Match match = matchMapper.toEntity(matchCreationDTO);
        match.setPlayer1(player1);
        match.setPlayer2(player2);

        return matchMapper.toDTO(matchRepository.save(match));
    }

    @Override
    public MatchDTO updateResult(long id, Boolean result) {
        Match existingMatch = matchRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
                        Collections.singletonMap("id", id)));

        if (existingMatch.getResult() != null) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("message", ErrorMessage.END_MATCH);
            errors.put("id", existingMatch.getId());

            throw new InvalidExceptionCustomize(errors);
        }

        PlayerProfileDTO player1ProfileDTO;
        PlayerProfileDTO player2ProfileDTO;

        if (result == null) {
            existingMatch.setResult(EMatchResult.DRAW.getValue());
            player1ProfileDTO = playerService.updateByMatchResult(
                    existingMatch.getPlayer1().getId(),
                    EMatchResult.DRAW.getValue(),
                    existingMatch.getEloBet());
            player2ProfileDTO = playerService.updateByMatchResult(
                    existingMatch.getPlayer2().getId(),
                    EMatchResult.DRAW.getValue(),
                    existingMatch.getEloBet());
        } else if (result) {
            existingMatch.setResult(EMatchResult.WIN.getValue());
            player1ProfileDTO = playerService.updateByMatchResult(
                    existingMatch.getPlayer1().getId(),
                    EMatchResult.WIN.getValue(),
                    existingMatch.getEloBet());
            player2ProfileDTO = playerService.updateByMatchResult(
                    existingMatch.getPlayer2().getId(),
                    EMatchResult.LOSE.getValue(),
                    existingMatch.getEloBet());
        } else {
            existingMatch.setResult(EMatchResult.LOSE.getValue());
            player1ProfileDTO = playerService.updateByMatchResult(
                    existingMatch.getPlayer1().getId(),
                    EMatchResult.LOSE.getValue(),
                    existingMatch.getEloBet());
            player2ProfileDTO = playerService.updateByMatchResult(
                    existingMatch.getPlayer2().getId(),
                    EMatchResult.WIN.getValue(),
                    existingMatch.getEloBet());
        }

        MatchDTO updatedMatchDTO = matchMapper.toDTO(
                matchRepository.save(existingMatch));
        updatedMatchDTO.setPlayer1ProfileDTO(player1ProfileDTO);
        updatedMatchDTO.setPlayer2ProfileDTO(player2ProfileDTO);

        return updatedMatchDTO;
    }
}
