package com.atquil.jwt_oauth2.repo;

import com.atquil.jwt_oauth2.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface eventUserRegisRepo extends JpaRepository<eventUserEntity, Long> {
}
