package com.atquil.jwt_oauth2.config.userConfig;

import com.atquil.jwt_oauth2.entity.UserInfoEntity;
import com.atquil.jwt_oauth2.repo.UserInfoRepo;
import com.atquil.jwt_oauth2.roles.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitialUserInfo implements CommandLineRunner {
    private final UserInfoRepo userInfoRepo;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        UserInfoEntity manager = new UserInfoEntity();
        manager.setUserName("Manager");
        manager.setRoles(Role.MANAGER);
        manager.setPassword(passwordEncoder.encode("managerPass"));
        manager.setEmailId("manager@manager.com");

        UserInfoEntity admin = new UserInfoEntity();
        admin.setUserName("Admin");
        admin.setRoles(Role.ADMIN);
        admin.setPassword(passwordEncoder.encode("adminPass"));
        admin.setEmailId("admin@admin.com");

        UserInfoEntity user = new UserInfoEntity();
        user.setUserName("User");
        user.setRoles(Role.USER);
        user.setPassword(passwordEncoder.encode("userPass"));
        user.setEmailId("user@user.com");

        UserInfoEntity user2 = new UserInfoEntity();
        user2.setUserName("Deepak");
        user2.setRoles(Role.USER);
        user2.setPassword(passwordEncoder.encode("1402"));
        user2.setEmailId("deepak@dpk.com");

        userInfoRepo.saveAll(List.of(manager,admin,user,user2));


    }
}
