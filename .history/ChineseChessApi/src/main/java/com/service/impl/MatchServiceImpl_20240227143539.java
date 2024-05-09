package com.service.impl;

import com.common.Default;
import com.common.ErrorMessage;
import com.common.enumeration.EMatchResult;
import com.config.exception.InvalidExceptionCustomize;
import com.config.exception.ResourceNotFoundExceptionCustomize;
import com.data.dto.match.MatchCreationDTO;
import com.data.dto.match.MatchDTO;
import com.data.dto.match.MatchDetailDTO;
import com.data.dto.move.MoveHistoryDTO;
import com.data.dto.player.PlayerProfileDTO;
import com.data.entity.Match;
import com.data.entity.Player;
import com.data.mapper.MatchMapper;
import com.data.repository.MatchRepository;
import com.data.repository.PlayerRepository;
import com.service.MatchService;
import com.service.MoveService;
import com.service.PlayerService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
      .map(m -> {
        MatchDTO matchDTO = matchMapper.toDTO(m);
        matchDTO.setPlayer1ProfileDTO(
          playerService.findById(m.getPlayer1().getId())
        );
        matchDTO.setPlayer2ProfileDTO(
          playerService.findById(m.getPlayer2().getId())
        );

        return matchDTO;
      })
      .collect(Collectors.toList());
  }

  @Override
  public MatchDTO findById(long id) {
    return matchRepository
      .findById(id)
      .map(m -> {
        MatchDTO matchDTO = matchMapper.toDTO(m);
        matchDTO.setPlayer1ProfileDTO(
          playerService.findById(m.getPlayer1().getId())
        );
        matchDTO.setPlayer2ProfileDTO(
          playerService.findById(m.getPlayer2().getId())
        );

        return matchDTO;
      })
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );
  }

  @Override
  public List<MatchDTO> findAllByPlayerId(long playerId) {
    return matchRepository
      .findAllByPlayerId(playerId)
      .stream()
      .map(m -> {
        MatchDTO matchDTO = matchMapper.toDTO(m);
        matchDTO.setPlayer1ProfileDTO(
          playerService.findById(m.getPlayer1().getId())
        );
        matchDTO.setPlayer2ProfileDTO(
          playerService.findById(m.getPlayer2().getId())
        );

        return matchDTO;
      })
      .collect(Collectors.toList());
  }

  @Override
  public MatchDetailDTO findDetailById(long id) {
    MatchDetailDTO matchDetailDTO = matchRepository
      .findById(id)
      .map(m -> {
        MatchDetailDTO mDetailDTO = matchMapper.toDetailDTO(m);
        mDetailDTO
          .getMatchDTO()
          .setPlayer1ProfileDTO(playerService.findById(m.getPlayer1().getId()));
        mDetailDTO
          .getMatchDTO()
          .setPlayer2ProfileDTO(playerService.findById(m.getPlayer2().getId()));

        return mDetailDTO;
      })
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );

    Map<Long, MoveHistoryDTO> moveHistoryDTOs = moveService.findAllByMatchId(id);

    matchDetailDTO.setTotalTurn((long) moveHistoryDTOs.size());
    matchDetailDTO.setMoveHistoryDTOs(moveHistoryDTOs);

    return matchDetailDTO;
  }

  @Override
  public MatchDTO create(MatchCreationDTO matchCreationDTO) {
    Player player1 = playerRepository
      .findById(matchCreationDTO.getPlayer1Id())
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("player1Id", matchCreationDTO.getPlayer1Id())
        )
      );

    Player player2 = playerRepository
      .findById(matchCreationDTO.getPlayer2Id())
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("player2Id", matchCreationDTO.getPlayer2Id())
        )
      );

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

    MatchDTO matchCreatedDTO = matchMapper.toDTO(
      matchRepository.save(matchMapper.toEntity(matchCreationDTO))
    );
    matchCreatedDTO.setPlayer1ProfileDTO(
      playerService.findById(player1.getId())
    );
    matchCreatedDTO.setPlayer2ProfileDTO(
      playerService.findById(player2.getId())
    );

    return matchCreatedDTO;
  }

  @Override
  public MatchDTO updateResult(long id, Boolean result) {
    Match existingMatch = matchRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );

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
      player1ProfileDTO = playerService.findById(existingMatch.getPlayer1().getId());
      player2ProfileDTO = playerService.findById(existingMatch.getPlayer2().getId());
    } else {
      int eloWin = (int) (
        Default.Game.ELO_WIN_RECEIVE_PERCENT * existingMatch.getEloBet()
      );
      int eloLose = existingMatch.getEloBet();

      if (result) {
        existingMatch.setResult(EMatchResult.WIN.getValue());
        player1ProfileDTO =
          playerService.update(
            existingMatch.getPlayer1().getId(),
            existingMatch.getPlayer1().getElo() + eloWin
          );

        player2ProfileDTO =
          playerService.update(
            existingMatch.getPlayer2().getId(),
            existingMatch.getPlayer2().getElo() - eloLose
          );
      } else {
        player1ProfileDTO =
          playerService.update(
            existingMatch.getPlayer1().getId(),
            existingMatch.getPlayer1().getElo() - eloLose
          );

        player2ProfileDTO =
          playerService.update(
            existingMatch.getPlayer2().getId(),
            existingMatch.getPlayer2().getElo() + eloWin
          );
      }
    }

    MatchDTO updatedMatchDTO = matchMapper.toDTO(
      matchRepository.save(existingMatch)
    );
    updatedMatchDTO.setPlayer1ProfileDTO(player1ProfileDTO);
    updatedMatchDTO.setPlayer2ProfileDTO(player2ProfileDTO);

    return updatedMatchDTO;
  }
}