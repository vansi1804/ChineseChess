package com.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.common.enumeration.EPiece;
import com.common.enumeration.ERole;
import com.config.exception.InternalServerErrorException;
import com.data.entity.Piece;
import com.data.entity.Player;
import com.data.entity.Rank;
import com.data.entity.Role;
import com.data.entity.User;
import com.data.entity.Vip;
import com.data.repository.PieceRepository;
import com.data.repository.PlayerRepository;
import com.data.repository.RankRepository;
import com.data.repository.RoleRepository;
import com.data.repository.UserRepository;
import com.data.repository.VipRepository;

@Service
public class ApplicationRunnerImpl implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VipRepository vipRepository;
    private final RankRepository rankRepository;
    private final PlayerRepository playerRepository;
    private final PieceRepository pieceRepository;

    @Autowired
    public ApplicationRunnerImpl(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            RoleRepository roleRepository,
            VipRepository vipRepository,
            RankRepository rankRepository,
            PlayerRepository playerRepository,
            PieceRepository pieceRepository) {

        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.vipRepository = vipRepository;
        this.rankRepository = rankRepository;
        this.playerRepository = playerRepository;
        this.pieceRepository = pieceRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initRoles();
        initVip();
        initRank();
        initAdminPlayer();
        innitPiece();
    }

    public void initRoles() {
        List<Role> roles = Arrays.stream(ERole.values())
                .filter(eRole -> !roleRepository.existsByName(eRole.name()))
                .map(eRole -> {
                    Role role = new Role();
                    role.setName(eRole.name());
                    return role;
                })
                .collect(Collectors.toList());

        if (!roles.isEmpty()) {
            roleRepository.saveAll(roles);
        }
    }

    public void initVip() {
        if (vipRepository.count() == 0) {
            vipRepository.save(new Vip(1, "Vip0", 0));
        }
    }

    public void initRank() {
        if (rankRepository.count() == 0) {
            rankRepository.save(new Rank(1, "Novice", 2000));
        }
    }

    private void initAdminPlayer() {
        User adminUser = initAdminUser();
        if (adminUser == null) {
            return;
        }

        Player adminPlayer = new Player();

        adminPlayer.setUser(adminUser);

        Rank defaultRank = rankRepository.findFirstByOrderByEloMilestonesAsc()
                .orElseThrow(() -> new InternalServerErrorException("default rank"));
        adminPlayer.setRank(defaultRank);

        adminPlayer.setElo(defaultRank.getEloMilestones());

        playerRepository.save(adminPlayer);
    }

    private User initAdminUser() {
        if (!userRepository.existsByPhoneNumber(Default.User.Admin.PHONE_NUMBER)) {

            User adminUser = new User();

            adminUser.setPhoneNumber(Default.User.Admin.PHONE_NUMBER);
            adminUser.setPassword(passwordEncoder.encode(Default.User.Admin.PASSWORD));
            adminUser.setName(Default.User.Admin.NAME);

            Role adminRole = roleRepository.findByName(Default.User.Admin.ROLE.name())
                    .orElseThrow(() -> new InternalServerErrorException("admin's role"));
            adminUser.setRole(adminRole);

            Vip adminVip = vipRepository.findFirstByOrderByDepositMilestonesDesc()
                    .orElseThrow(() -> new InternalServerErrorException("admin's vip"));
            adminUser.setVip(adminVip);

            adminUser.setStatus(Default.User.Admin.STATUS.name());

            return userRepository.save(adminUser);
        }

        return null;
    }

    private void innitPiece() {
        List<Piece> defaultPieces = new ArrayList<>();
        // Red pieces
        defaultPieces.add(
                new Piece(1, EPiece.SOLDIER.name(), true, "red_soldier.png", 0, 6));
        defaultPieces.add(
                new Piece(2, EPiece.SOLDIER.name(), true, "red_soldier.png", 2, 6));
        defaultPieces.add(
                new Piece(3, EPiece.SOLDIER.name(), true, "red_soldier.png", 4, 6));
        defaultPieces.add(
                new Piece(4, EPiece.SOLDIER.name(), true, "red_soldier.png", 6, 6));
        defaultPieces.add(
                new Piece(5, EPiece.SOLDIER.name(), true, "red_soldier.png", 8, 6));
        defaultPieces.add(
                new Piece(6, EPiece.CANNON.name(), true, "red_cannon.png", 1, 7));
        defaultPieces.add(
                new Piece(7, EPiece.CANNON.name(), true, "red_cannon.png", 7, 7));
        defaultPieces.add(
                new Piece(8, EPiece.CHARIOT.name(), true, "red_chariot.png", 0, 9));
        defaultPieces.add(
                new Piece(9, EPiece.CHARIOT.name(), true, "red_chariot.png", 8, 9));
        defaultPieces.add(
                new Piece(10, EPiece.HORSE.name(), true, "red_horse.png", 1, 9));
        defaultPieces.add(
                new Piece(11, EPiece.HORSE.name(), true, "red_horse.png", 7, 9));
        defaultPieces.add(
                new Piece(12, EPiece.ELEPHANT.name(), true, "red_elephant.png", 2, 9));
        defaultPieces.add(
                new Piece(13, EPiece.ELEPHANT.name(), true, "red_elephant.png", 6, 9));
        defaultPieces.add(
                new Piece(14, EPiece.GUARD.name(), true, "red_guard.png", 3, 9));
        defaultPieces.add(
                new Piece(15, EPiece.GUARD.name(), true, "red_guard.png", 5, 9));
        defaultPieces.add(
                new Piece(16, EPiece.GENERAL.name(), true, "red_general.png", 4, 9));

        // Black pieces
        defaultPieces.add(
                new Piece(17, EPiece.SOLDIER.name(), false, "black_soldier.png", 0, 3));
        defaultPieces.add(
                new Piece(18, EPiece.SOLDIER.name(), false, "black_soldier.png", 2, 3));
        defaultPieces.add(
                new Piece(19, EPiece.SOLDIER.name(), false, "black_soldier.png", 4, 3));
        defaultPieces.add(
                new Piece(20, EPiece.SOLDIER.name(), false, "black_soldier.png", 6, 3));
        defaultPieces.add(
                new Piece(21, EPiece.SOLDIER.name(), false, "black_soldier.png", 8, 3));
        defaultPieces.add(
                new Piece(22, EPiece.CANNON.name(), false, "black_cannon.png", 1, 2));
        defaultPieces.add(
                new Piece(23, EPiece.CANNON.name(), false, "black_cannon.png", 7, 2));
        defaultPieces.add(
                new Piece(24, EPiece.CHARIOT.name(), false, "black_chariot.png", 0, 0));
        defaultPieces.add(
                new Piece(25, EPiece.CHARIOT.name(), false, "black_chariot.png", 8, 0));
        defaultPieces.add(
                new Piece(26, EPiece.HORSE.name(), false, "black_horse.png", 1, 0));
        defaultPieces.add(
                new Piece(27, EPiece.HORSE.name(), false, "black_horse.png", 7, 0));
        defaultPieces.add(
                new Piece(28, EPiece.ELEPHANT.name(), false, "black_elephant.png", 2, 0));
        defaultPieces.add(
                new Piece(29, EPiece.ELEPHANT.name(), false, "black_elephant.png", 6, 0));
        defaultPieces.add(
                new Piece(30, EPiece.GUARD.name(), false, "black_guard.png", 3, 0));
        defaultPieces.add(
                new Piece(31, EPiece.GUARD.name(), false, "black_guard.png", 5, 0));
        defaultPieces.add(
                new Piece(32, EPiece.GENERAL.name(), false, "black_general.png", 4, 0));

        pieceRepository.saveAll(defaultPieces);
    }

}