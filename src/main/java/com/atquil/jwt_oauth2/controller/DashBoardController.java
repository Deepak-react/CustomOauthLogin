package com.atquil.jwt_oauth2.controller;

import com.atquil.jwt_oauth2.dto.*;
import com.atquil.jwt_oauth2.entity.*;
import com.atquil.jwt_oauth2.logClass.*;
import com.atquil.jwt_oauth2.repo.*;
import com.atquil.jwt_oauth2.service.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import jakarta.servlet.http.*;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.server.*;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.security.Principal;
import java.util.*;

@RestController


@RequestMapping("/api")
@RequiredArgsConstructor
public class DashBoardController {
    private final AuthService authService;
    private final LogFile logFile;
    private final EmpRepo empRepo;
    private final EventRepo eventRepo;
    private static final String UPLOADED_FOLDER = "uploaded-images/";
    private static final String EVENT_IMAGES = "event-images/";


    private static final Logger log = LoggerFactory.getLogger(DashBoardController.class);

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/welcome-message")
    public ResponseEntity<String> getWelcomeMessage(Authentication authentication) {
        logFile.basicLogs(authentication);
        authService.validateToken();
        return ResponseEntity.ok("Welcome to the JWT Tutorial:" + authentication.getName() + " with scope:" + authentication.getAuthorities());
    }


    @GetMapping("/manager-message")
    @PreAuthorize("hasAuthority('SCOPE_READ') and hasAuthority('SCOPE_WRITE')")
        public ResponseEntity<String> getManagerData(Principal principal) {
        return ResponseEntity.ok("Manager::" + principal.getName());

    }

    @PreAuthorize("hasAuthority('SCOPE_READ') and hasAuthority('SCOPE_WRITE') and hasAuthority('SCOPE_DELETE') and hasAuthority('SCOPE_UPDATE')")
    @GetMapping("/admin-message")
    public ResponseEntity<String> getAdminData(Principal principal) {

        return ResponseEntity.ok("Admin::" + principal.getName() + " has this message:");

    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
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

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/view")
    public ResponseEntity<String> viewEmployeeList(HttpServletRequest request) {
        List<EmployeeEntity> response = empRepo.fetchAllData();
        return ResponseEntity.ok("Employee details: " + response);
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("/add")
    public ResponseEntity<String> addEmployee(@RequestBody @Valid EmpDto empDto) {
        logFile.basicLogs("Entering into addEmployee file.....");
        logFile.basicLogs(empDto);

        authService.empDetails(empDto);
        return ResponseEntity.ok("Employee added successfully!!!");
    }

    @PreAuthorize("hasAuthority('SCOPE_UPDATE')")
        @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable(value = "id") Long empId, @RequestBody @Valid EmpDto empDto) {
        Optional<EmployeeEntity> employee = empRepo.findById(empId);
        authService.empDetails(empDto, employee);
        return ResponseEntity.ok("Employee updated successfully!!!");
    }

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<String> deleteEmployee(@PathVariable(value = "id" )Long empId) {
        Optional<EmployeeEntity> employeeEntity = empRepo.findById(empId);
        EmployeeEntity employeeEntity1 = employeeEntity.get();
        String name = employeeEntity1.getEmpName();
        empRepo.deleteEmployee(empId);
        return ResponseEntity.ok("Employee, " + name + " has been deleted successfully!!!");
    }

    @GetMapping("/dashboard")
    public ResponseEntity<String> dashBoardMessage(HttpServletRequest request,  Authentication authentication) {
            String role = authService.getRole();
            System.out.println("The Dashboard role is: "+ role);
            if(role.contains("ROLE_USER")){
                System.out.println("Entering into the user function...");
                    return getWelcomeMessage(authentication);
            } else if (role.contains("ROLE_MANAGER")) {
                       return getManagerData(authentication);
            } else if (role.contains("ROLE_ADMIN")) {
                return getAdminData(authentication);
            }
            else {
                throw new RuntimeException("User role not found in access token!!");
            }
    }

    @PostMapping("/upload")
    public String upload(
                @RequestParam("file") MultipartFile file , @RequestParam("name") String name){
        if(file.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file selected");
        }try {
            File directory= new File(UPLOADED_FOLDER);
            if(!directory.exists()){
                directory.mkdirs();
            }
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, file.getBytes());
            return "File uploaded successfully: " + path.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/registerEvent")
    public EventEntity registerEvent(
            @RequestParam("poster_image") MultipartFile poster_image,@RequestParam("banner_image") MultipartFile banner_image ,@RequestParam("eventDto") String eventDto) throws JsonProcessingException {
        if(poster_image.isEmpty() && banner_image.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file selected");
        }
        ObjectMapper objectMapper = new ObjectMapper();

        EventDto event = objectMapper.readValue( eventDto, EventDto.class);

        try {
            File directory= new File(EVENT_IMAGES);
            if(!directory.exists()){
                directory.mkdirs();
            }

            List<String> paths = authService.createEventImageFile(event.getEventName() , poster_image, banner_image);
            String posterImagePath = paths.get(0);
            String bannerImagePath = paths.get(1);
            EventEntity eventEntity = authService.addEventDetails(event , posterImagePath, bannerImagePath);

            return eventEntity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/fetchEventDetails")
    public ResponseEntity<List<EventEntity>> fetchEventDetails(HttpServletRequest request) {
        List<EventEntity> response = eventRepo.findAll();
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("/userEventRegis")
    public ResponseEntity<String> userEventRegistration(@RequestBody @Valid EmpDto empDto) {
        logFile.basicLogs("Entering into addEmployee file.....");
        logFile.basicLogs(empDto);

        authService.empDetails(empDto);
        return ResponseEntity.ok("Employee added successfully!!!");
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/fetchEventByEmail")
    public EventEntity fetchAppDetails(@RequestParam("email") String email){
        return authService.fetchEventDetails(email);
    }
}


