package com.service.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.common.Default;
import com.common.enumeration.EResult;
import com.common.enumeration.ERole;
import com.data.dto.player.PlayerCreationDTO;
import com.data.dto.player.PlayerDTO;
import com.data.dto.player.PlayerProfileDTO;
import com.data.dto.user.UserDTO;
import com.data.dto.user.UserProfileDTO;
import com.data.entity.Player;
import com.data.entity.Rank;
import com.config.exception.InternalServerErrorException;
import com.config.exception.ResourceNotFoundException;
import com.data.mapper.PlayerMapper;
import com.data.repository.RankRepository;
import com.data.repository.PlayerRepository;
import com.service.PlayerService;
import com.service.UserService;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final UserService userService;
    private final RankRepository rankRepository;

    @Autowired
    public PlayerServiceImpl(
            PlayerRepository playerRepository,
            PlayerMapper playerMapper,
            UserService userService,
            RankRepository rankRepository) {

        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.userService = userService;
        this.rankRepository = rankRepository;
    }

    @Override
    public Page<PlayerDTO> findAll(int no, int limit, String sortBy) {
        return playerRepository.findAll(PageRequest.of(no, limit, Sort.by(sortBy)))
                .map(p -> playerMapper.toDTO(p));
    }

    @Override
    public PlayerDTO findByUserId(long userId) {
        return playerRepository.findByUser_Id(userId)
                .map(p -> playerMapper.toDTO(p))
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("user.id", userId)));
    }

    @Override
    public PlayerProfileDTO findById(long id) {
        return playerRepository.findById(id)
                .map(p -> playerMapper.toProfileDTO(p))
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("id", id)));
    }

    @Override
    public PlayerDTO create(PlayerCreationDTO playerCreationDTO, MultipartFile fileAvatar) {
        UserDTO createdUserDTO = userService.create(
                playerCreationDTO.getUserCreationDTO(), fileAvatar, ERole.PLAYER);

        Player player = playerMapper.toEntity(playerCreationDTO);
        player.getUser().setId(createdUserDTO.getId());

        Rank defaultRank = rankRepository.findFirstByOrderByEloMilestonesAsc()
                .orElseThrow(
                        () -> new InternalServerErrorException("No rank found"));
        player.setRank(defaultRank);

        player.setElo(defaultRank.getEloMilestones());

        PlayerDTO createdPlayerDTO = playerMapper.toDTO(playerRepository.save(player));
        createdPlayerDTO.setUserDTO(createdUserDTO);
        return createdPlayerDTO;
    }

    @Override
    public PlayerProfileDTO update(long id, PlayerProfileDTO playerProfileDTO, MultipartFile fileAvatar) {
        Player oldPlayer = playerRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("id", id)));

        if (!userService.isCurrentUser(oldPlayer.getUser().getId())) {
            throw new AccessDeniedException(null);
        }

        UserProfileDTO updatedUserProfileDTO = userService.update(
                oldPlayer.getUser().getId(), playerProfileDTO.getUserProfileDTO(), fileAvatar);

        Player updatePlayer = playerMapper.toEntity(playerProfileDTO);
        updatePlayer.setId(oldPlayer.getId());
        updatePlayer.getUser().setId(oldPlayer.getUser().getId());
        updatePlayer.setRank(oldPlayer.getRank());
        updatePlayer.setElo(oldPlayer.getElo());
        updatePlayer.setWin(oldPlayer.getWin());
        updatePlayer.setDraw(oldPlayer.getDraw());
        updatePlayer.setLose(oldPlayer.getLose());

        PlayerProfileDTO updatedPlayerProfileDTO = playerMapper.toProfileDTO(updatePlayer);
        updatedPlayerProfileDTO.setUserProfileDTO(updatedUserProfileDTO);
        return updatedPlayerProfileDTO;
    }

    @Override
    public PlayerProfileDTO updateByEloBetAndResult(long id, int eloBet, EResult eResult) {
        Player updatePlayer = playerRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("id", id)));

        switch (eResult) {
            case WIN:
                updatePlayer.setElo((int) (updatePlayer.getElo() + eloBet * Default.Game.ELO_WIN_RECEIVE_PERCENT));
                updatePlayer.setWin(updatePlayer.getWin() + 1);
                break;

            case LOSE:
                updatePlayer.setElo((int) (updatePlayer.getElo() - eloBet));
                updatePlayer.setLose(updatePlayer.getLose() + 1);
                break;

            default:
                updatePlayer.setDraw(updatePlayer.getDraw() + 1);
        }

        Rank rank = rankRepository.findFirstByEloMilestonesLessThanEqualOrderByEloMilestonesDesc(eloBet)
                .orElseThrow(
                        () -> new InternalServerErrorException("No rank found for updating for player by elo"));
        updatePlayer.setRank(rank);

        return playerMapper.toProfileDTO(updatePlayer);
    }

}
