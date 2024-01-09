package com.service.impl;

import com.common.enumeration.ERole;
import com.config.exception.InternalServerErrorExceptionCustomize;
import com.config.exception.ResourceNotFoundExceptionCustomize;
import com.data.dto.RankDTO;
import com.data.dto.player.PlayerCreationDTO;
import com.data.dto.player.PlayerDTO;
import com.data.dto.player.PlayerOthersInfoDTO;
import com.data.dto.player.PlayerProfileDTO;
import com.data.dto.user.UserDTO;
import com.data.dto.user.UserProfileDTO;
import com.data.entity.Player;
import com.data.entity.Rank;
import com.data.mapper.PlayerMapper;
import com.data.mapper.RankMapper;
import com.data.repository.MatchRepository;
import com.data.repository.PlayerRepository;
import com.data.repository.RankRepository;
import com.service.PlayerService;
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
  private final RankRepository rankRepository;
  private final RankMapper rankMapper;
  private final MatchRepository matchRepository;

  @Override
  public Page<PlayerDTO> findAll(int no, int limit, String sortBy) {
    return playerRepository
      .findAll(PageRequest.of(no, limit, Sort.by(sortBy)))
      .map(p -> {
        PlayerDTO playerDTO = playerMapper.toDTO(p);
        playerDTO.setPlayerOthersInfoDTO(this.buildPlayerOthersInfoDTO(p));

        return playerDTO;
      });
  }

  @Override
  public PlayerDTO findByUserId(long userId) {
    return playerRepository
      .findByUser_Id(userId)
      .map(p -> {
        PlayerDTO playerDTO = playerMapper.toDTO(p);
        playerDTO.setPlayerOthersInfoDTO(this.buildPlayerOthersInfoDTO(p));

        return playerDTO;
      })
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("user.id", userId)
        )
      );
  }

  @Override
  public PlayerProfileDTO findById(long id) {
    return playerRepository
      .findById(id)
      .map(p -> {
        PlayerProfileDTO playerProfileDTO = playerMapper.toProfileDTO(p);
        playerProfileDTO.setPlayerOthersInfoDTO(
          this.buildPlayerOthersInfoDTO(p)
        );

        return playerProfileDTO;
      })
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

    Rank defaultRank = rankRepository
      .findFirstByOrderByEloMilestonesAsc()
      .orElseThrow(() ->
        new InternalServerErrorExceptionCustomize("No rank found")
      );
    player.setElo(defaultRank.getEloMilestones());

    PlayerDTO createdPlayerDTO = playerMapper.toDTO(
      playerRepository.save(player)
    );
    createdPlayerDTO.setUserDTO(createdUserDTO);
    createdPlayerDTO.setPlayerOthersInfoDTO(
      this.buildPlayerOthersInfoDTO(player)
    );

    return createdPlayerDTO;
  }

  @Override
  public PlayerProfileDTO update(long id, PlayerProfileDTO playerProfileDTO) {
    Player oldPlayer = playerRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );

    if (!userService.isCurrentUser(oldPlayer.getUser().getId())) {
      throw new AccessDeniedException(null);
    }

    UserProfileDTO updatedUserProfileDTO = userService.update(
      oldPlayer.getUser().getId(),
      playerProfileDTO.getUserProfileDTO()
    );

    Player updatePlayer = playerMapper.toEntity(playerProfileDTO);
    updatePlayer.setId(oldPlayer.getId());
    updatePlayer.getUser().setId(oldPlayer.getUser().getId());
    updatePlayer.setElo(oldPlayer.getElo());

    PlayerProfileDTO updatedPlayerProfileDTO = playerMapper.toProfileDTO(
      updatePlayer
    );
    updatedPlayerProfileDTO.setUserProfileDTO(updatedUserProfileDTO);
    updatedPlayerProfileDTO.setPlayerOthersInfoDTO(
      this.buildPlayerOthersInfoDTO(updatePlayer)
    );

    return updatedPlayerProfileDTO;
  }

  @Override
  public PlayerProfileDTO update(long id, int elo) {
    Player oldPlayer = playerRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );

    oldPlayer.setElo(elo);

    PlayerProfileDTO updatedPlayerProfileDTO = playerMapper.toProfileDTO(
      playerRepository.save(oldPlayer)
    );
    updatedPlayerProfileDTO.setPlayerOthersInfoDTO(
      this.buildPlayerOthersInfoDTO(oldPlayer)
    );

    return updatedPlayerProfileDTO;
  }

  private PlayerOthersInfoDTO buildPlayerOthersInfoDTO(Player player) {
    PlayerOthersInfoDTO playerOthersInfoDTO = playerMapper.toOthersInfoDTO(
      player
    );

    RankDTO rankDTO = rankRepository
      .findFirstByEloMilestonesLessThanEqualOrderByEloMilestonesDesc(
        player.getElo()
      )
      .map(r -> rankMapper.toDTO(r))
      .orElseThrow(() ->
        new InternalServerErrorExceptionCustomize(
          "No rank found for elo: " + player.getElo()
        )
      );
    playerOthersInfoDTO.setRankDTO(rankDTO);

    long win = matchRepository.countWinByPlayerId(player.getId());
    playerOthersInfoDTO.setWin(win);

    long draw = matchRepository.countDrawByPlayerId(player.getId());
    playerOthersInfoDTO.setDraw(draw);

    long lose = matchRepository.countLoseByPlayerId(player.getId());
    playerOthersInfoDTO.setLose(lose);

    return playerOthersInfoDTO;
  }
}
