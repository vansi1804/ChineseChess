package com.nvs.service.impl;

import com.nvs.common.Default;
import com.nvs.common.enumeration.EPiece;
import com.nvs.common.enumeration.ERole;
import com.nvs.config.exception.InternalServerErrorExceptionCustomize;
import com.nvs.data.entity.Piece;
import com.nvs.data.entity.Player;
import com.nvs.data.entity.Rank;
import com.nvs.data.entity.Role;
import com.nvs.data.entity.User;
import com.nvs.data.entity.Vip;
import com.nvs.data.repository.PieceRepository;
import com.nvs.data.repository.PlayerRepository;
import com.nvs.data.repository.RankRepository;
import com.nvs.data.repository.RoleRepository;
import com.nvs.data.repository.UserRepository;
import com.nvs.data.repository.VipRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationRunnerImpl implements ApplicationRunner {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final VipRepository vipRepository;
  private final RankRepository rankRepository;
  private final PlayerRepository playerRepository;
  private final PieceRepository pieceRepository;

  @Override
  public void run(ApplicationArguments args) {
    log.info("Starting application initialization...");
    this.initRole();
    this.initVip();
    this.initRank();
    this.initAdminPlayer();
    this.innitPiece();
    log.info("Application initialization completed.");
  }

  private void initRole() {
    log.info("Initializing roles...");
    List<Role> roles = Arrays.stream(ERole.values())
        .filter(eRole -> !roleRepository.existsByName(eRole.name())).map(eRole -> {
          Role role = new Role();
          role.setName(eRole.name());
          log.debug("Creating role: {}", eRole.name());
          return role;
        }).collect(Collectors.toList());

    if (!roles.isEmpty()) {
      roleRepository.saveAll(roles);
      log.info("Saved {} roles to the database.", roles.size());
    } else {
      log.info("No new roles to initialize.");
    }
  }

  private void initVip() {
    log.info("Initializing VIPs...");
    if (vipRepository.count() == 0) {
      vipRepository.save(new Vip(1, "Vip0", 0));
      log.info("Default VIP initialized.");
    } else {
      log.info("VIPs already initialized.");
    }
  }

  private void initRank() {
    log.info("Initializing ranks...");
    if (rankRepository.count() == 0) {
      rankRepository.save(new Rank(1, "Novice", 2000));
      log.info("Default rank initialized.");
    } else {
      log.info("Ranks already initialized.");
    }
  }

  private void initAdminPlayer() {
    log.info("Initializing admin player...");
    User adminUser = initAdminUser();
    if (adminUser == null) {
      log.info("Admin player already exists. Skipping admin player initialization.");
      return;
    }

    Player adminPlayer = new Player();
    adminPlayer.setUser(adminUser);

    Rank defaultRank = rankRepository.findFirstByOrderByEloMilestonesAsc().orElseThrow(() -> {
      log.error("No rank found for initializing admin player.");
      return new InternalServerErrorExceptionCustomize("No rank found");
    });
    adminPlayer.setElo(defaultRank.getEloMilestones());

    playerRepository.save(adminPlayer);
    log.info("Admin player initialized with default rank: {}", defaultRank.getName());
  }

  private void innitPiece() {
    log.info("Initializing game pieces...");
    List<Piece> defaultPieces = new ArrayList<>();

    // Red pieces
    log.debug("Adding red pieces...");
    defaultPieces.add(new Piece(1, EPiece.SOLDIER.name(), true, "red_soldier.png", 0, 6));
    defaultPieces.add(new Piece(2, EPiece.SOLDIER.name(), true, "red_soldier.png", 2, 6));
    defaultPieces.add(new Piece(3, EPiece.SOLDIER.name(), true, "red_soldier.png", 4, 6));
    defaultPieces.add(new Piece(4, EPiece.SOLDIER.name(), true, "red_soldier.png", 6, 6));
    defaultPieces.add(new Piece(5, EPiece.SOLDIER.name(), true, "red_soldier.png", 8, 6));
    defaultPieces.add(new Piece(6, EPiece.CANNON.name(), true, "red_cannon.png", 1, 7));
    defaultPieces.add(new Piece(7, EPiece.CANNON.name(), true, "red_cannon.png", 7, 7));
    defaultPieces.add(new Piece(8, EPiece.CHARIOT.name(), true, "red_chariot.png", 0, 9));
    defaultPieces.add(new Piece(9, EPiece.CHARIOT.name(), true, "red_chariot.png", 8, 9));
    defaultPieces.add(new Piece(10, EPiece.HORSE.name(), true, "red_horse.png", 1, 9));
    defaultPieces.add(new Piece(11, EPiece.HORSE.name(), true, "red_horse.png", 7, 9));
    defaultPieces.add(new Piece(12, EPiece.ELEPHANT.name(), true, "red_elephant.png", 2, 9));
    defaultPieces.add(new Piece(13, EPiece.ELEPHANT.name(), true, "red_elephant.png", 6, 9));
    defaultPieces.add(new Piece(14, EPiece.GUARD.name(), true, "red_guard.png", 3, 9));
    defaultPieces.add(new Piece(15, EPiece.GUARD.name(), true, "red_guard.png", 5, 9));
    defaultPieces.add(new Piece(16, EPiece.GENERAL.name(), true, "red_general.png", 4, 9));

    // Black pieces
    log.debug("Adding black pieces...");
    defaultPieces.add(new Piece(17, EPiece.SOLDIER.name(), false, "black_soldier.png", 0, 3));
    defaultPieces.add(new Piece(18, EPiece.SOLDIER.name(), false, "black_soldier.png", 2, 3));
    defaultPieces.add(new Piece(19, EPiece.SOLDIER.name(), false, "black_soldier.png", 4, 3));
    defaultPieces.add(new Piece(20, EPiece.SOLDIER.name(), false, "black_soldier.png", 6, 3));
    defaultPieces.add(new Piece(21, EPiece.SOLDIER.name(), false, "black_soldier.png", 8, 3));
    defaultPieces.add(new Piece(22, EPiece.CANNON.name(), false, "black_cannon.png", 1, 2));
    defaultPieces.add(new Piece(23, EPiece.CANNON.name(), false, "black_cannon.png", 7, 2));
    defaultPieces.add(new Piece(24, EPiece.CHARIOT.name(), false, "black_chariot.png", 0, 0));
    defaultPieces.add(new Piece(25, EPiece.CHARIOT.name(), false, "black_chariot.png", 8, 0));
    defaultPieces.add(new Piece(26, EPiece.HORSE.name(), false, "black_horse.png", 1, 0));
    defaultPieces.add(new Piece(27, EPiece.HORSE.name(), false, "black_horse.png", 7, 0));
    defaultPieces.add(new Piece(28, EPiece.ELEPHANT.name(), false, "black_elephant.png", 2, 0));
    defaultPieces.add(new Piece(29, EPiece.ELEPHANT.name(), false, "black_elephant.png", 6, 0));
    defaultPieces.add(new Piece(30, EPiece.GUARD.name(), false, "black_guard.png", 3, 0));
    defaultPieces.add(new Piece(31, EPiece.GUARD.name(), false, "black_guard.png", 5, 0));
    defaultPieces.add(new Piece(32, EPiece.GENERAL.name(), false, "black_general.png", 4, 0));

    pieceRepository.saveAll(defaultPieces);
    log.info("Initialized {} game pieces.", defaultPieces.size());
  }

  private User initAdminUser() {
    log.info("Checking if admin user exists...");
    if (!userRepository.existsByPhoneNumber(Default.User.Admin.PHONE_NUMBER)) {
      log.info("Admin user does not exist. Initializing admin user...");
      User adminUser = new User();
      adminUser.setPhoneNumber(Default.User.Admin.PHONE_NUMBER);
      adminUser.setPassword(passwordEncoder.encode(Default.User.Admin.PASSWORD));
      adminUser.setName(Default.User.Admin.NAME);

      Role adminRole = roleRepository.findByName(Default.User.Admin.ROLE.name()).orElseThrow(() -> {
        log.error("No role found for admin user.");
        return new InternalServerErrorExceptionCustomize("No role found");
      });
      adminUser.setRole(adminRole);

      Vip adminVip = vipRepository.findFirstByOrderByDepositMilestonesDesc().orElseThrow(() -> {
        log.error("No VIP found for admin user.");
        return new InternalServerErrorExceptionCustomize("No vip found");
      });
      adminUser.setVip(adminVip);

      adminUser.setStatus(Default.User.Admin.STATUS.name());

      User savedAdminUser = userRepository.save(adminUser);
      log.info("Admin user initialized with phone number: {}", savedAdminUser.getPhoneNumber());
      return savedAdminUser;
    }

    log.info("Admin user already exists. Skipping initialization.");
    return null;
  }

}
