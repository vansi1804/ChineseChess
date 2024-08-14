package com.nvs.service.impl;

import com.nvs.common.enumeration.EMatchResult;
import com.nvs.config.exception.InvalidExceptionCustomize;
import com.nvs.config.exception.ResourceNotFoundExceptionCustomize;
import com.nvs.config.i18nMessage.Translator;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchServiceImpl implements MatchService {

  private final MatchRepository matchRepository;
  private final MatchMapper matchMapper;
  private final PlayerRepository playerRepository;
  private final PlayerService playerService;
  private final MoveService moveService;

  @Override
  @Cacheable(value = "matches")
  public List<MatchDTO> findAll() {
    log.info("Fetching all matches");
    return matchRepository.findAll().stream().map(matchMapper::toDTO).collect(Collectors.toList());
  }

  @Override
  @Cacheable(value = "match", key = "#id")
  public MatchDTO findById(long id) {
    log.info("Fetching match by id: {}", id);
    return matchRepository.findById(id)
        .map(matchMapper::toDTO)
        .orElseThrow(() -> {
          log.error("Match with id {} not found", id);
          return new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id));
        });
  }

  @Override
  @Cacheable(value = "matchesByPlayer", key = "#playerId")
  public List<MatchDTO> findAllByPlayerId(long playerId) {
    log.info("Fetching matches for player id: {}", playerId);
    return matchRepository.findAllByPlayerId(playerId).stream()
        .map(matchMapper::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(value = "matchDetails", key = "#id")
  public MatchDetailDTO findDetailById(long id) {
    log.info("Fetching match details by id: {}", id);
    MatchDetailDTO matchDetailDTO = matchRepository.findById(id)
        .map(matchMapper::toDetailDTO)
        .orElseThrow(() -> {
          log.error("Match details with id {} not found", id);
          return new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id));
        });

    Map<Long, MoveHistoryDTO> moveHistoryDTOs = moveService.findAllByMatchId(id);
    matchDetailDTO.setTotalTurn((long) moveHistoryDTOs.size());
    matchDetailDTO.setMoveHistoryDTOs(moveHistoryDTOs);

    return matchDetailDTO;
  }

  @Override
  @CachePut(value = "match")
  public MatchDTO create(MatchCreationDTO matchCreationDTO) {
    log.info("Creating match with players: {} and {}", matchCreationDTO.getPlayer1Id(),
        matchCreationDTO.getPlayer2Id());

    Player player1 = playerRepository.findById(matchCreationDTO.getPlayer1Id())
        .orElseThrow(() -> {
          log.error("Player with id {} not found", matchCreationDTO.getPlayer1Id());
          return new ResourceNotFoundExceptionCustomize(
              Collections.singletonMap("player1Id", matchCreationDTO.getPlayer1Id()));
        });

    Player player2 = playerRepository.findById(matchCreationDTO.getPlayer2Id())
        .orElseThrow(() -> {
          log.error("Player with id {} not found", matchCreationDTO.getPlayer2Id());
          return new ResourceNotFoundExceptionCustomize(
              Collections.singletonMap("player2Id", matchCreationDTO.getPlayer2Id()));
        });

    if (matchRepository.existsPlayingByPlayerId(player1.getId())) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", Translator.toLocale("PLAYER_PLAYING"));
      errors.put("player1Id", player1.getId());

      log.error("Player with id {} is already playing a match", player1.getId());
      throw new InvalidExceptionCustomize(errors);
    }

    if (matchRepository.existsPlayingByPlayerId(player2.getId())) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", Translator.toLocale("PLAYER_PLAYING"));
      errors.put("player2Id", player2.getId());

      log.error("Player with id {} is already playing a match", player2.getId());
      throw new InvalidExceptionCustomize(errors);
    }

    Match match = matchMapper.toEntity(matchCreationDTO);
    match.setPlayer1(player1);
    match.setPlayer2(player2);

    MatchDTO createdMatchDTO = matchMapper.toDTO(matchRepository.save(match));
    log.info("Match created successfully with id: {}", createdMatchDTO.getId());

    return createdMatchDTO;
  }

  @Override
  @CachePut(value = "matches", key = "#id")
  public MatchDTO updateResult(long id, Boolean result) {
    log.info("Updating result for match id: {}", id);
    Match existingMatch = matchRepository.findById(id)
        .orElseThrow(() -> {
          log.error("Match with id {} not found", id);
          return new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id));
        });

    if (existingMatch.getResult() != null) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", Translator.toLocale("END_MATCH"));
      errors.put("id", existingMatch.getId());

      log.error("Match with id {} has already ended", existingMatch.getId());
      throw new InvalidExceptionCustomize(errors);
    }

    PlayerProfileDTO player1ProfileDTO;
    PlayerProfileDTO player2ProfileDTO;

    if (result == null) {
      existingMatch.setResult(EMatchResult.DRAW.getValue());
      player1ProfileDTO = playerService.updateByMatchResult(existingMatch.getPlayer1().getId(),
          EMatchResult.DRAW.getValue(), existingMatch.getEloBet());
      player2ProfileDTO = playerService.updateByMatchResult(existingMatch.getPlayer2().getId(),
          EMatchResult.DRAW.getValue(), existingMatch.getEloBet());
    } else if (result) {
      existingMatch.setResult(EMatchResult.WIN.getValue());
      player1ProfileDTO = playerService.updateByMatchResult(existingMatch.getPlayer1().getId(),
          EMatchResult.WIN.getValue(), existingMatch.getEloBet());
      player2ProfileDTO = playerService.updateByMatchResult(existingMatch.getPlayer2().getId(),
          EMatchResult.LOSE.getValue(), existingMatch.getEloBet());
    } else {
      existingMatch.setResult(EMatchResult.LOSE.getValue());
      player1ProfileDTO = playerService.updateByMatchResult(existingMatch.getPlayer1().getId(),
          EMatchResult.LOSE.getValue(), existingMatch.getEloBet());
      player2ProfileDTO = playerService.updateByMatchResult(existingMatch.getPlayer2().getId(),
          EMatchResult.WIN.getValue(), existingMatch.getEloBet());
    }

    MatchDTO updatedMatchDTO = matchMapper.toDTO(matchRepository.save(existingMatch));
    updatedMatchDTO.setPlayer1ProfileDTO(player1ProfileDTO);
    updatedMatchDTO.setPlayer2ProfileDTO(player2ProfileDTO);

    log.info("Match result updated successfully for match id: {}", updatedMatchDTO.getId());
    return updatedMatchDTO;
  }

}
