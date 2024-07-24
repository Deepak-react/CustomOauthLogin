package com.atquil.jwt_oauth2.service;

import com.atquil.jwt_oauth2.dto.*;
import com.atquil.jwt_oauth2.entity.*;
import com.atquil.jwt_oauth2.repo.*;
import jakarta.servlet.http.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.logout.*;
import org.springframework.stereotype.*;

import java.time.*;



@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutHandlerService implements LogoutHandler {
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserActivityRepo userActivityRepo;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)  {



        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!authHeader.startsWith(TokenType.Bearer.name())){
            return;
        }

        final String refreshToken = authHeader.substring(7);

//        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken);


        try {
            Authentication newAuth = getAuthenticationFromRefreshToken(refreshToken);

            customLogUserActivity(newAuth.getName());

            var storedRefreshToken = refreshTokenRepo.findByRefreshToken(refreshToken)
                    .map(token -> {
                        token.setRevoked(true);
                        refreshTokenRepo.save(token);
                        return token;
                    })
                    .orElse(null);

        } catch (Exception e) {
            log.error("Error during logout", e);
        }
        response.setStatus(HttpServletResponse.SC_OK);


    }

    private Authentication getAuthenticationFromRefreshToken(String refreshToken) {
        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (refreshTokenEntity.isRevoked()) {
            throw new IllegalArgumentException("Refresh token is revoked");
        }

        UserInfoEntity userInfoEntity = refreshTokenEntity.getUser();

        return new UsernamePasswordAuthenticationToken(userInfoEntity.getUserName(),userInfoEntity, null);
    }
    private void customLogUserActivity(String username) {
        UserActivityEntity activity = new UserActivityEntity();
        activity.setUsername(username);
        activity.setActivityType("LOGOUT");
        activity.setTimeStamp(LocalDateTime.now());
        userActivityRepo.save(activity);
    }



}

