package com.atquil.jwt_oauth2.controller;

import com.atquil.jwt_oauth2.dto.*;
import com.atquil.jwt_oauth2.service.*;
import jakarta.servlet.http.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.core.*;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j

public class AuthController {
    private final AuthService authService;
    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(Authentication authentication, HttpServletResponse response, HttpServletRequest request){
        String Ipaddress = request.getRemoteAddr();
         Boolean value= authService.logUserActivity(authentication.getName(), "LOGIN" , Ipaddress);

        if (value){
            return null;
        }
        else {
            AuthResponseDto token_response = authService.getJwtTokensAfterAuthentication(authentication, response );
            authService.setTokenValue(token_response);
            return ResponseEntity.ok(token_response); //1
        }
    }
        @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
        @PostMapping("/refresh-token")
        public ResponseEntity<AuthResponseDto> refreshToken(HttpServletRequest request) {

            AuthResponseDto response = authService.refreshAccessToken(request);
            return ResponseEntity.ok(response);
        }


    }
