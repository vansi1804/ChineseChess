package com.service.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.common.enumeration.ERole;
import com.common.enumeration.EStatus;
import com.config.exception.ResourceNotFoundException;
import com.data.entity.Player;
import com.data.entity.User;
import com.data.repository.PlayerRepository;
import com.data.repository.RankRepository;
import com.data.repository.RoleRepository;
import com.data.repository.UserRepository;
import com.data.repository.VipRepository;

@Service
public class AdminInitializerServiceImpl implements ApplicationRunner {

    private static final String PHONE_NUMBER = "0589176839";
    private static final String PASSWORD = "admin";
    private static final String NAME = "Admin";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VipRepository vipRepository;
    private final RankRepository rankRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public AdminInitializerServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            VipRepository vipRepository,
            RankRepository rankRepository,
            PlayerRepository playerRepository,
            PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.vipRepository = vipRepository;
        this.rankRepository = rankRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initAdminPlayer();
    }

    private void initAdminPlayer() {
        User adminUser = initAdminUser();
        if (adminUser == null) {
            return;
        }

        Player adminPlayer = new Player();

        adminPlayer.setUser(adminUser);
        adminPlayer.setRank(rankRepository.findByName(Default.Player.RANK.name())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("rank.name", Default.Player.RANK.name()))));
        adminPlayer.setElo(Default.Player.ELO);

        playerRepository.save(adminPlayer);
    }

    private User initAdminUser() {
        if (userRepository.existsByPhoneNumber(PHONE_NUMBER)) {
            return null;
        }

        User adminUser = new User();

        adminUser.setPhoneNumber(PHONE_NUMBER);
        adminUser.setPassword(passwordEncoder.encode(PASSWORD));
        adminUser.setName(NAME);
        adminUser.setRole(
                roleRepository.findByName(ERole.ADMIN.name())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                Collections.singletonMap("user.role.name", ERole.ADMIN.name()))));
        adminUser.setVip(
                vipRepository.findByName(Default.User.VIP.name())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                Collections.singletonMap("user.vip.name", Default.User.VIP.name()))));
        adminUser.setStatus(EStatus.ACTIVE.name());

        return userRepository.save(adminUser);
    }

}