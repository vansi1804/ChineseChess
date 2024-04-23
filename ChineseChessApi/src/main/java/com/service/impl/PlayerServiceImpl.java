package com.service.impl;

import com.common.Default;
import com.common.enumeration.EMatchResult;
import com.common.enumeration.ERole;
import com.config.exception.ResourceNotFoundExceptionCustomize;
import com.data.dto.RankDTO;
import com.data.dto.player.PlayerCreationDTO;
import com.data.dto.player.PlayerDTO;
import com.data.dto.player.PlayerProfileDTO;
import com.data.dto.user.UserDTO;
import com.data.dto.user.UserProfileDTO;
import com.data.entity.Player;
import com.data.entity.Rank;
import com.data.mapper.PlayerMapper;
import com.data.repository.PlayerRepository;
import com.service.PlayerService;
import com.service.RankService;
import com.service.UserService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

  private final PlayerRepository playerRepository;
  private final PlayerMapper playerMapper;
  private final UserService userService;
  private final RankService rankService;

  @Override
  public Page<PlayerDTO> findAll(int no, int limit, String sortBy) {
    return playerRepository
      .findAll(PageRequest.of(no, limit, Sort.by(sortBy)))
      .map(p -> playerMapper.toDTO(p));
  }

  @Override
  public PlayerDTO findByUserId(long userId) {
    return playerRepository
      .findByUser_Id(userId)
      .map(p -> playerMapper.toDTO(p))
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("userId", userId)
        )
      );
  }

  @Override
  public PlayerProfileDTO findById(long id) {
    return playerRepository
      .findById(id)
      .map(p -> playerMapper.toProfileDTO(p))
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );
  }

  @Override
  public PlayerDTO create(PlayerCreationDTO playerCreationDTO) {
    UserDTO createdUserDTO = userService.create(
      playerCreationDTO.getUserCreationDTO(),
      ERole.PLAYER
    );

    Player player = playerMapper.toEntity(playerCreationDTO);
    player.getUser().setId(createdUserDTO.getId());

    RankDTO defaultRank = rankService.findDefault();
    player.setRank(new Rank());
    player.getRank().setId(defaultRank.getId());

    player.setElo(defaultRank.getEloMilestones());

    PlayerDTO createdPlayerDTO = playerMapper.toDTO(
      playerRepository.save(player)
    );
    createdPlayerDTO.setUserDTO(createdUserDTO);
    createdPlayerDTO.getPlayerOthersInfoDTO().setRankDTO(defaultRank);

    return createdPlayerDTO;
  }

  @Override
  public PlayerProfileDTO update(long id, PlayerProfileDTO playerProfileDTO) {
    Player existingPlayer = playerRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );

    if (!userService.isCurrentUser(existingPlayer.getUser().getId())) {
      throw new AccessDeniedException(null);
    }

    UserProfileDTO updatedUserProfileDTO = userService.update(
      existingPlayer.getUser().getId(),
      playerProfileDTO.getUserProfileDTO()
    );

    Player updatePlayer = playerMapper.toEntity(playerProfileDTO);
    updatePlayer.setId(existingPlayer.getId());
    updatePlayer.getUser().setId(existingPlayer.getUser().getId());
    updatePlayer.setElo(existingPlayer.getElo());

    PlayerProfileDTO updatedPlayerProfileDTO = playerMapper.toProfileDTO(
      updatePlayer
    );
    updatedPlayerProfileDTO.setUserProfileDTO(updatedUserProfileDTO);

    return updatedPlayerProfileDTO;
  }

  @Override
  public PlayerProfileDTO updateByMatchResult(long id, int result, int eloBet) {
    Player existingPlayer = playerRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );

    if (EMatchResult.WIN.getValue() == result) {
      existingPlayer.setWin(existingPlayer.getWin() + 1);
      existingPlayer.setElo(
        existingPlayer.getElo() +
        (int) (eloBet * Default.Game.ELO_WIN_RECEIVE_PERCENT)
      );
    } else if (EMatchResult.LOSE.getValue() == result) {
      existingPlayer.setLose(existingPlayer.getLose() + 1);
      existingPlayer.setElo(existingPlayer.getElo() - eloBet);
    } else {
      existingPlayer.setDraw(existingPlayer.getDraw() + 1);
    }

    RankDTO rankDTO = rankService.findByPlayerElo(existingPlayer.getElo());
    existingPlayer.getRank().setId(rankDTO.getId());

    PlayerProfileDTO updatedPlayerProfileDTO = playerMapper.toProfileDTO(
      playerRepository.save(existingPlayer)
    );
    updatedPlayerProfileDTO.getPlayerOthersInfoDTO().setRankDTO(rankDTO);

    return updatedPlayerProfileDTO;
  }
}
