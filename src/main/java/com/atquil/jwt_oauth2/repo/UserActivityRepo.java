package com.atquil.jwt_oauth2.repo;

import com.atquil.jwt_oauth2.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import java.util.*;
@Repository
public interface UserActivityRepo extends JpaRepository<UserActivityEntity, Long> {
    @Query("SELECT u.username FROM UserActivityEntity u WHERE u.activityType= :activity")
    List<String> findListofUsernameByActivity(@Param("activity") String activity);
}
