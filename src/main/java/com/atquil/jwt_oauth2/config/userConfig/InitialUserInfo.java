package com.atquil.jwt_oauth2.config.userConfig;

import com.atquil.jwt_oauth2.entity.UserInfoEntity;
import com.atquil.jwt_oauth2.repo.UserInfoRepo;
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
        manager.setPassword(passwordEncoder.encode("managerPass"));
        manager.setRoles("ROLE_MANAGER");
        manager.setEmailId("manager@manager.com");

        UserInfoEntity admin = new UserInfoEntity();
        admin.setUserName("Admin");
        admin.setPassword(passwordEncoder.encode("adminPass"));
        admin.setRoles("ROLE_ADMIN");
        admin.setEmailId("admin@admin.com");

        UserInfoEntity user = new UserInfoEntity();
        user.setUserName("User");
        user.setPassword(passwordEncoder.encode("userPass"));
        user.setRoles("ROLE_USER");
        user.setEmailId("user@user.com");

        userInfoRepo.saveAll(List.of(manager,admin,user));


    }
}
