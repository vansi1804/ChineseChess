package com.service.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.common.Default;
import com.common.enumeration.ERole;
import com.common.enumeration.EStatus;
import com.data.dto.PlayerDTO;
import com.data.dto.PlayerCreationDTO;
import com.data.dto.PlayerProfileDTO;
import com.data.dto.UserDTO;
import com.data.dto.UserProfileDTO;
import com.data.entity.Player;
import com.exception.ResourceNotFoundException;
import com.data.mapper.PlayerMapper;
import com.data.repository.RankRepository;
import com.data.repository.MatchRepository;
import com.data.repository.PlayerRepository;
import com.service.PlayerService;
import com.service.UserService;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final UserService userService;
    private final MatchRepository matchRepository;
    private final RankRepository rankRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository,
            PlayerMapper playerMapper,
            UserService userService,
            MatchRepository matchRepository,
            RankRepository rankRepository) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.userService = userService;
        this.matchRepository = matchRepository;
        this.rankRepository = rankRepository;
    }

    @Override
    public Page<PlayerDTO> findAll(int no, int limit, String sortBy) {
        return playerRepository.findAll(PageRequest.of(no, limit, Sort.by(sortBy)))
                .map(p -> playerMapper.toDTO(p, matchRepository.findAllByPlayerId(p.getId())));
    }

    @Override
    public PlayerDTO findByUserId(long userId) {
        return playerRepository.findByUser_Id(userId)
                .map(p -> playerMapper.toDTO(p, matchRepository.findAllByPlayerId(p.getId())))
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("user.id", userId)));
    }

    @Override
    public PlayerProfileDTO findById(long id) {
        return playerRepository.findById(id)
                .map(p -> playerMapper.toProfileDTO(p, matchRepository.findAllByPlayerId(p.getId())))
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));
    }

    @Override
    public PlayerDTO create(PlayerCreationDTO playerCreationDTO, MultipartFile fileAvatar) {
        UserDTO createdUserDTO = userService.create(playerCreationDTO.getUserCreationDTO(), fileAvatar,
                ERole.PLAYER);

        Player player = playerMapper.toEntity(playerCreationDTO);
        player.getUser().setId(createdUserDTO.getId());
        String levelDefault = Default.User.LEVEL.name();
        player.setRank(rankRepository.findByName(levelDefault)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("level", levelDefault))));

        PlayerDTO createdPlayerDTO = playerMapper.toDTO(playerRepository.save(player), null);
        createdPlayerDTO.setUserDTO(createdUserDTO);
        return createdPlayerDTO;
    }

    @Override
    public PlayerProfileDTO update(long id, PlayerProfileDTO playerProfileDTO, MultipartFile fileAvatar) {
        Player oldPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        UserProfileDTO updatedUserProfileDTO = userService.update(
                oldPlayer.getUser().getId(), playerProfileDTO.getUserProfileDTO(), fileAvatar);

        Player updatePlayer = playerMapper.toEntity(playerProfileDTO);
        updatePlayer.setId(oldPlayer.getId());
        updatePlayer.getUser().setId(oldPlayer.getUser().getId());
        updatePlayer.setRank(oldPlayer.getRank());
        updatePlayer.setElo(oldPlayer.getElo());

        PlayerProfileDTO updatedPlayerProfileDTO = playerMapper.toProfileDTO(
                updatePlayer, matchRepository.findAllByPlayerId(updatePlayer.getId()));
        updatedPlayerProfileDTO.setUserProfileDTO(updatedUserProfileDTO);
        return updatedPlayerProfileDTO;
    }

    @Override
    public PlayerProfileDTO updateEloById(long id, int elo) {
        Player updatePlayer = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));
        updatePlayer.setElo(elo);
        return playerMapper.toProfileDTO(updatePlayer, matchRepository.findAllByPlayerId(updatePlayer.getId()));
    }

    @Override
    public PlayerDTO lockById(long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        PlayerDTO playerDTO = playerMapper.toDTO(player, matchRepository.findAllByPlayerId(player.getId()));
        UserDTO lockedUserDTO = userService.updateStatusById(player.getUser().getId(), EStatus.LOCK);
        playerDTO.setUserDTO(lockedUserDTO);
        return playerDTO;
    }

    @Override
    public PlayerDTO unlockById(long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        PlayerDTO playerDTO = playerMapper.toDTO(player, matchRepository.findAllByPlayerId(player.getId()));
        UserDTO lockedUserDTO = userService.updateStatusById(player.getUser().getId(), EStatus.ACTIVE);
        playerDTO.setUserDTO(lockedUserDTO);
        return playerDTO;
    }

}
