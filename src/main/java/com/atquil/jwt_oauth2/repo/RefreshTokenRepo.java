package com.atquil.jwt_oauth2.repo;

import com.atquil.jwt_oauth2.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshTokenEntity, Long> {

    @Query(value = "SELECT rt.* FROM REFRESH_TOKENS rt " +
            "INNER JOIN USER_DETAILS ud ON rt.user_id = ud.id " +
            "WHERE ud.EMAIL = :userEmail and rt.revoked = false ", nativeQuery = true)
    List<RefreshTokenEntity> findAll(String userEmail);
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
