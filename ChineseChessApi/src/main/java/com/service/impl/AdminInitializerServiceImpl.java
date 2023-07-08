package com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.config.exception.InternalServerErrorException;
import com.data.entity.Player;
import com.data.entity.Rank;
import com.data.entity.Role;
import com.data.entity.User;
import com.data.entity.Vip;
import com.data.repository.PlayerRepository;
import com.data.repository.RankRepository;
import com.data.repository.RoleRepository;
import com.data.repository.UserRepository;
import com.data.repository.VipRepository;

@Service
public class AdminInitializerServiceImpl implements ApplicationRunner {

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

        Rank defaultRank = rankRepository.findFirstByOrderByMilestonesAsc()
                .orElseThrow(() -> new InternalServerErrorException("default rank"));
        adminPlayer.setRank(defaultRank);
        adminPlayer.setElo(defaultRank.getMilestones());

        playerRepository.save(adminPlayer);
    }

    private User initAdminUser() {
        User existingAdminUser = userRepository.findByPhoneNumber(Default.User.Admin.PHONE_NUMBER).orElse(null);

        if (existingAdminUser == null) {

            User adminUser = new User();

            adminUser.setPhoneNumber(Default.User.Admin.PHONE_NUMBER);
            adminUser.setPassword(passwordEncoder.encode(Default.User.Admin.PASSWORD));
            adminUser.setName(Default.User.Admin.NAME);

            Role adminRole = roleRepository.findByName(Default.User.Admin.ROLE.name())
                    .orElseThrow(() -> new InternalServerErrorException("admin's role"));
            adminUser.setRole(adminRole);

            Vip defaultVip = vipRepository.findFirstByOrderByDepositMilestonesDesc()
                    .orElseThrow(() -> new InternalServerErrorException("admin's vip"));
            adminUser.setVip(defaultVip);

            adminUser.setStatus(Default.User.Admin.STATUS.name());

            return userRepository.save(adminUser);
        } else {
            Vip defaultVip = vipRepository.findFirstByOrderByDepositMilestonesDesc()
                    .orElseThrow(() -> new InternalServerErrorException("admin's vip"));
            existingAdminUser.setVip(defaultVip);

            return null;
        }
    }

}