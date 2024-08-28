package com.atquil.jwt_oauth2.repo;

import com.atquil.jwt_oauth2.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository

public interface EventRepo extends JpaRepository<EventEntity, Long > {
    @Query("SELECT e FROM EventEntity e WHERE e.emailId= :emailId")
    Optional<EventEntity>  findByEmailId(@Param("emailId") String emailId);
}
