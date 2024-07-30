package com.atquil.jwt_oauth2.controller;

import com.atquil.jwt_oauth2.dto.*;
import com.atquil.jwt_oauth2.entity.*;
import com.atquil.jwt_oauth2.logClass.*;
import com.atquil.jwt_oauth2.repo.*;
import com.atquil.jwt_oauth2.service.*;
import jakarta.servlet.http.*;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController

@RequestMapping("/api")
@RequiredArgsConstructor
public class DashBoardController {
    private final AuthService authService;
    private final LogFile logFile;
    private final EmpRepo empRepo;
    private static final Logger logger = LoggerFactory.getLogger(DashBoardController.class);


    private static final Logger log = LoggerFactory.getLogger(DashBoardController.class);

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/welcome-message")

    public ResponseEntity<String> getWelcomeMessage(Authentication authentication) {
        return ResponseEntity.ok("Welcome to the JWT Tutorial:" + authentication.getName() + " with scope:" + authentication.getAuthorities());
    }


    @GetMapping("/manager-message")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<String> getManagerData(Principal principal) {
        return ResponseEntity.ok("Manager::" + principal.getName());

    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("/admin-message")
    public ResponseEntity<String> getAdminData(Principal principal) {
        return ResponseEntity.ok("Admin::" + principal.getName() + " has this message:");

    }


    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("/menu")
    public ResponseEntity<String> menuItems(HttpServletRequest request) {
        logFile.basicLogs("Entering into the menu api");
        boolean isValid = authService.validateToken();
        logFile.basicLogs(isValid);
        if (isValid) {
            String accessToken = authService.fetchAccessToken(request);
            boolean validateNewToken = authService.validateToken(accessToken);
            if (!validateNewToken) {
                List<String> items = authService.menuItems();
                return ResponseEntity.ok("The items are :" + items);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        } else {
            List<String> items = authService.menuItems();
            return ResponseEntity.ok("The items are :" + items);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @GetMapping("/view")
    public ResponseEntity<String> viewEmployeeList(HttpServletRequest request) {
        List<EmployeeEntity> response = empRepo.fetchAllData();
        return ResponseEntity.ok("Employee details: {}" + response);
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("/add")
    public ResponseEntity<String> addEmployee(@RequestBody @Valid EmpDto empDto) {
        logFile.basicLogs("Entering into addEmployee file.....");
        logFile.basicLogs(empDto);

        authService.empDetails(empDto);
        return ResponseEntity.ok("Employee added successfully!!!");
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable(value = "id") Long empId, @RequestBody @Valid EmpDto empDto) {
        Optional<EmployeeEntity> employee = empRepo.findById(empId);
        authService.empDetails(empDto, employee);
        return ResponseEntity.ok("Employee updated successfully!!!");
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<String> deleteEmployee(@PathVariable(value = "id" )Long empId) {
        Optional<EmployeeEntity> employeeEntity = empRepo.findById(empId);
        EmployeeEntity employeeEntity1 = employeeEntity.get();
        String name = employeeEntity1.getEmpName();
        empRepo.deleteEmployee(empId);
        return ResponseEntity.ok("Employee, " + name + " has been deleted successfully!!!");



    }

}