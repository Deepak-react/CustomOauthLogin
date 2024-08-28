package com.atquil.jwt_oauth2.config.userConfig;
import com.atquil.jwt_oauth2.repo.UserInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class UserInfoManagerConfig implements UserDetailsService {

    private final UserInfoRepo userInfoRepo;

    public UserInfoManagerConfig(UserInfoRepo userInfoRepo) {
        this.userInfoRepo = userInfoRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String emailID) throws UsernameNotFoundException {
        return userInfoRepo.findByEmailId(emailID).map(UserInfoConfig::new).orElseThrow(()-> new UsernameNotFoundException("UserEmail: "+emailID+" does not exist"));
    }
}

// In here we are fetching the particular data (userEntity) from the repository and mapping it to userDetails...
// So that it can display the userName, password of  an entity once the UserinfoConfig class is called...