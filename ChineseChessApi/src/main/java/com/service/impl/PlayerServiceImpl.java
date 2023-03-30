package com.service.impl;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.common.ErrorMessage;
import com.common.enumeration.EStatus;
import com.data.dto.PlayerDTO;
import com.data.dto.creation.PlayerCreationDTO;
import com.data.dto.profile.PlayerProfileDTO;
import com.data.entity.Player;
import com.exception.ExceptionCustom;
import com.helper.Encoding;
import com.data.mapper.PlayerMapper;
import com.data.repository.LevelsRepository;
import com.data.repository.PlayerRepository;
import com.data.repository.RoleRepository;
import com.data.repository.UserRepository;
import com.data.repository.VipRepository;
import com.service.PlayerService;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerMapper playerMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private VipRepository vipRepository;
    @Autowired
    private LevelsRepository levelsRepository;

    @Override
    public List<PlayerDTO> findAll() {
        return playerRepository.findAll().stream().map(p -> playerMapper.toDTO(p)).collect(Collectors.toList());
    }

    @Override
    public PlayerDTO findById(long id) {
        return playerRepository.findById(id).map(p -> playerMapper.toDTO(p))
                .orElseThrow(() -> new ExceptionCustom("Player", ErrorMessage.DATA_NOT_FOUND, "id", id));
    }

    @Override
    public PlayerProfileDTO create(PlayerCreationDTO playerCreationDTO) throws NoSuchAlgorithmException {
        if (userRepository.findByPhoneNumber(playerCreationDTO.getUserCreationDTO().getPhoneNumber()).isPresent()) {
            throw new ExceptionCustom("Player", ErrorMessage.DATA_EXISTING, "phone number",
                    playerCreationDTO.getUserCreationDTO().getPhoneNumber());
        }
        Player player = playerMapper.toEntity(playerCreationDTO);
        player.getUser().setCreatedAt(LocalDateTime.now());
        player.getUser().setPassword(Encoding.getMD5(playerCreationDTO.getUserCreationDTO().getPassword()));
        player.getUser().setRole(roleRepository.findById(Default.ROLE_PLAYER_ID).get());
        player.getUser().setVip(vipRepository.findById(Default.VIP0_ID).get());
        player.getUser().setStatus(EStatus.Active.getValue());
        player.setLevels(levelsRepository.findById(Default.LEVELS_ID).get());
        player.setElo(player.getLevels().getMilestones());
        userRepository.save(player.getUser());
        return playerMapper.toProfileDTO(playerRepository.save(player));
    }

    @Override
    public PlayerProfileDTO findProfileById(long id) {
        return playerRepository.findById(id).map(p -> playerMapper.toProfileDTO(p))
                .orElseThrow(
                        () -> new ExceptionCustom("Player", ErrorMessage.DATA_NOT_FOUND, "id", id));
    }

    @Override
    public PlayerProfileDTO updateProfileById(long id, PlayerProfileDTO playerProfileDTO) {
        return playerRepository.findById(id)
                .map(p -> {
                    p.getUser().setName(playerProfileDTO.getUserProfileDTO().getName());
                    p.getUser().setAvatar(playerProfileDTO.getUserProfileDTO().getAvatar());
                    userRepository.save(p.getUser());
                    return playerMapper.toProfileDTO(p);
                })
                .orElseThrow(
                        () -> new ExceptionCustom("Player", ErrorMessage.DATA_NOT_FOUND, "id", id));
    }

    @Override
    public PlayerProfileDTO updateEloById(long id, int elo) {
        return playerRepository.findById(id)
                .map(p -> {
                    p.setElo(elo);
                    return playerMapper.toProfileDTO(playerRepository.save(p));
                })
                .orElseThrow(
                        () -> new ExceptionCustom("Player", ErrorMessage.DATA_NOT_FOUND, "id", id));
    }

    @Override
    public PlayerDTO lockById(long id) {
        return playerRepository.findById(id)
                .map(p -> {
                    p.getUser().setStatus(EStatus.Locked.getValue());
                    userRepository.save(p.getUser());
                    return playerMapper.toDTO(p);
                })
                .orElseThrow(
                        () -> new ExceptionCustom("Player", ErrorMessage.DATA_NOT_FOUND, "id", id));
    }

    @Override
    public PlayerDTO unlockById(long id) {
        return playerRepository.findById(id)
                .map(p -> {
                    p.getUser().setStatus(EStatus.Active.getValue());
                    userRepository.save(p.getUser());
                    return playerMapper.toDTO(p);
                })
                .orElseThrow(
                        () -> new ExceptionCustom("Player", ErrorMessage.DATA_NOT_FOUND, "id", id));
    }

}
