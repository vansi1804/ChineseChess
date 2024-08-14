package com.nvs.service.impl;

import com.nvs.common.Default;
import com.nvs.common.enumeration.EMatchResult;
import com.nvs.common.enumeration.ERole;
import com.nvs.config.exception.ResourceNotFoundExceptionCustomize;
import com.nvs.data.dto.RankDTO;
import com.nvs.data.dto.player.PlayerCreationDTO;
import com.nvs.data.dto.player.PlayerDTO;
import com.nvs.data.dto.player.PlayerProfileDTO;
import com.nvs.data.dto.user.UserDTO;
import com.nvs.data.dto.user.UserProfileDTO;
import com.nvs.data.entity.Player;
import com.nvs.data.entity.Rank;
import com.nvs.data.mapper.PlayerMapper;
import com.nvs.data.repository.PlayerRepository;
import com.nvs.service.PlayerService;
import com.nvs.service.RankService;
import com.nvs.service.UserService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerServiceImpl implements PlayerService {

  private final PlayerRepository playerRepository;
  private final PlayerMapper playerMapper;
  private final UserService userService;
  private final RankService rankService;

  @Cacheable(value = "players", key = "#no + '-' + #limit + '-' + #sortBy")
  @Override
  public Page<PlayerDTO> findAll(int no, int limit, String sortBy) {
    log.debug("Finding all players with page number: {}, page size: {}, sort by: {}", no, limit,
        sortBy);
    Page<PlayerDTO> playersPage = playerRepository.findAll(
            PageRequest.of(no, limit, Sort.by(sortBy)))
        .map(playerMapper::toDTO);
    log.debug("Found {} players.", playersPage.getTotalElements());
    return playersPage;
  }

  @Cacheable(value = "playerByUserId", key = "#userId")
  @Override
  public PlayerDTO findByUserId(long userId) {
    log.debug("Finding player by user ID: {}", userId);
    PlayerDTO playerDTO = playerRepository.findByUser_Id(userId).map(playerMapper::toDTO)
        .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("userId", userId)));
    log.debug("Found player: {}", playerDTO);
    return playerDTO;
  }

  @Cacheable(value = "playerProfileById", key = "#id")
  @Override
  public PlayerProfileDTO findById(long id) {
    log.debug("Finding player by ID: {}", id);
    PlayerProfileDTO playerProfileDTO = playerRepository.findById(id)
        .map(playerMapper::toProfileDTO).orElseThrow(
            () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));
    log.debug("Found player profile: {}", playerProfileDTO);
    return playerProfileDTO;
  }

  @CachePut(value = "player", key = "#playerCreationDTO.userCreationDTO.phoneNumber")
  @Override
  public PlayerDTO create(PlayerCreationDTO playerCreationDTO) {
    log.debug("Creating player with details: {}", playerCreationDTO);

    UserDTO createdUserDTO = userService.create(playerCreationDTO.getUserCreationDTO(),
        ERole.PLAYER);

    Player player = playerMapper.toEntity(playerCreationDTO);
    player.getUser().setId(createdUserDTO.getId());

    RankDTO defaultRank = rankService.findDefault();
    player.setRank(new Rank());
    player.getRank().setId(defaultRank.getId());

    player.setElo(defaultRank.getEloMilestones());

    PlayerDTO createdPlayerDTO = playerMapper.toDTO(playerRepository.save(player));
    createdPlayerDTO.setUserDTO(createdUserDTO);
    createdPlayerDTO.getPlayerOthersInfoDTO().setRankDTO(defaultRank);

    log.debug("Player created successfully: {}", createdPlayerDTO);
    return createdPlayerDTO;
  }

  @CachePut(value = "playerProfileById", key = "#id")
  @Override
  public PlayerProfileDTO update(long id, PlayerProfileDTO playerProfileDTO) {
    log.debug("Updating player with ID: {} and details: {}", id, playerProfileDTO);

    Player existingPlayer = playerRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));

    if (userService.isCurrentUser(existingPlayer.getUser().getId())) {
      throw new AccessDeniedException(
          "Access denied for user ID: " + existingPlayer.getUser().getId());
    }

    UserProfileDTO updatedUserProfileDTO = userService.update(existingPlayer.getUser().getId(),
        playerProfileDTO.getUserProfileDTO());

    Player updatePlayer = playerMapper.toEntity(playerProfileDTO);
    updatePlayer.setId(existingPlayer.getId());
    updatePlayer.getUser().setId(existingPlayer.getUser().getId());
    updatePlayer.setElo(existingPlayer.getElo());

    PlayerProfileDTO updatedPlayerProfileDTO = playerMapper.toProfileDTO(updatePlayer);
    updatedPlayerProfileDTO.setUserProfileDTO(updatedUserProfileDTO);

    log.debug("Player profile updated successfully: {}", updatedPlayerProfileDTO);
    return updatedPlayerProfileDTO;
  }

  @CachePut(value = "playerProfileById", key = "#id")
  @Override
  public PlayerProfileDTO updateByMatchResult(long id, int result, int eloBet) {
    log.debug("Updating player by match result with ID: {}, result: {}, elo bet: {}", id, result,
        eloBet);

    Player existingPlayer = playerRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id)));

    if (EMatchResult.WIN.getValue() == result) {
      existingPlayer.setWin(existingPlayer.getWin() + 1);
      existingPlayer.setElo(
          existingPlayer.getElo() + (int) (eloBet * Default.Game.ELO_WIN_RECEIVE_PERCENT));
    } else if (EMatchResult.LOSE.getValue() == result) {
      existingPlayer.setLose(existingPlayer.getLose() + 1);
      existingPlayer.setElo(existingPlayer.getElo() - eloBet);
    } else {
      existingPlayer.setDraw(existingPlayer.getDraw() + 1);
    }

    RankDTO rankDTO = rankService.findByPlayerElo(existingPlayer.getElo());
    existingPlayer.getRank().setId(rankDTO.getId());

    PlayerProfileDTO updatedPlayerProfileDTO = playerMapper.toProfileDTO(
        playerRepository.save(existingPlayer));
    updatedPlayerProfileDTO.getPlayerOthersInfoDTO().setRankDTO(rankDTO);

    log.debug("Player profile updated by match result successfully: {}", updatedPlayerProfileDTO);
    return updatedPlayerProfileDTO;
  }

}
