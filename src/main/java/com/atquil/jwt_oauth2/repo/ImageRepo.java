package com.atquil.jwt_oauth2.repo;

import com.atquil.jwt_oauth2.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface ImageRepo extends JpaRepository<ImageEntity, Long > {
}
