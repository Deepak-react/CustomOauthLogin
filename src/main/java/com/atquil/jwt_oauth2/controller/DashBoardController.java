package com.atquil.jwt_oauth2.controller;

import com.atquil.jwt_oauth2.dto.*;
import com.atquil.jwt_oauth2.logClass.*;
import com.atquil.jwt_oauth2.service.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;

import java.security.Principal;
import java.util.*;

@RestController

@RequestMapping("/api")
@RequiredArgsConstructor
public class DashBoardController {
    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final LogFile logFile;

    private static final Logger log = LoggerFactory.getLogger(DashBoardController.class);

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/welcome-message")

    public ResponseEntity<String> getWelcomeMessage(Authentication authentication) {
        return ResponseEntity.ok("Welcome to the JWT Tutorial:" + authentication.getName() + " with scope:" + authentication.getAuthorities());
    }

//    @PreAuthorize("hasRole('ROLE_MANAGER')")

    @GetMapping("/manager-message")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
        public ResponseEntity<String> getManagerData(Principal principal) {
            return ResponseEntity.ok("Manager::" + principal.getName());

        }
        @PreAuthorize("hasAuthority('SCOPE_WRITE')")
        @PostMapping("/admin-message")
        public ResponseEntity<String> getAdminData( Principal principal) {
            return ResponseEntity.ok("Admin::" + principal.getName() + " has this message:" );

        }


        @PreAuthorize("hasAuthority('SCOPE_WRITE')")
        @PostMapping("/menu")
        public ResponseEntity<String> menuItems(HttpServletRequest request){
            logFile.basicLogs("Entering into the menu api");
            boolean isValid = authService.validateToken();
            logFile.basicLogs(isValid);
            if(isValid){
                    String accessToken = authService.fetchAccessToken(request);
                    boolean validateNewToken = authService.validateToken(accessToken);
                    if(!validateNewToken){
                        List<String> items = authService.menuItems();
                        return ResponseEntity.ok("The items are :"+items);}
                    else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
                    }
            }
            else{
                List<String> items = authService.menuItems();
                return ResponseEntity.ok("The items are :"+items);
            }
        };

}