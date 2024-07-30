package com.atquil.jwt_oauth2.repo;

import com.atquil.jwt_oauth2.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;
import java.util.*;

@Repository
public interface EmpRepo extends JpaRepository<EmployeeEntity, Long> {
    @Query("SELECT e FROM EmployeeEntity e ")
    List<EmployeeEntity> fetchAllData();

    @Modifying
    @Query("DELETE FROM EmployeeEntity e WHERE e.id = :empId")
    void deleteEmployee(@Param("empId") Long empId);
    
}
