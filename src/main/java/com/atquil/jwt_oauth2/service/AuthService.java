package com.atquil.jwt_oauth2.service;

import com.atquil.jwt_oauth2.config.jwtConfig.*;
import com.atquil.jwt_oauth2.dto.*;
import com.atquil.jwt_oauth2.entity.*;
import com.atquil.jwt_oauth2.logClass.*;
import com.atquil.jwt_oauth2.repo.*;
import com.atquil.jwt_oauth2.roles.*;
import com.auth0.jwt.*;
import com.auth0.jwt.interfaces.*;
import jakarta.persistence.*;
import jakarta.servlet.http.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;
import org.springframework.web.multipart.*;
import org.springframework.web.server.*;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final RestTemplate restTemplate;
    private final UserInfoRepo userInfoRepo;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserActivityRepo userActivityRepo;
    private final LogFile logFile;
    private final EmpRepo empRepo;
    private final ImageRepo imageRepo;
    private final PasswordEncoder passwordEncoder;
    private final EventRepo eventRepo;
        





    public String fetchAccessToken(HttpServletRequest request) {
        AuthResponseDto responseBody = refreshAccessToken(request);
        String newAccessToken = responseBody.getAccessToken();


        return null;
    }

    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try {
            Optional<UserInfoEntity> userInfoEntity1 = userInfoRepo.findByEmailId(authentication.getName());
            log.info("ENTITY:{}", userInfoEntity1);
            var userInfoEntity = userInfoRepo.findByEmailId(authentication.getName())
                    .orElseThrow(() -> {
                        log.error("[AuthService:userSignInAuth] User :{} not found", authentication.getName());
                        log.info("Authentication OBJECTS:{}", authentication);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND ");
                    });
            log.info("User Entity: {}", userInfoEntity);

            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);
            creatRefreshTokenCookie(response, refreshToken, userInfoEntity);


            saveUserRefreshToken(userInfoEntity, refreshToken);

            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated", userInfoEntity.getUserName());
            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(userInfoEntity.getUserName())
                    .emailId(userInfoEntity.getEmailId())
                    .role(userInfoEntity.getRoles())
                    .tokenType(TokenType.Bearer)
                    .build();


        } catch (Exception e) {
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :{}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
        }
    }

    private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken, UserInfoEntity userInfo) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60); // in seconds
        response.addCookie(refreshTokenCookie);
        logFile.cookieLog(refreshTokenCookie, userInfo);
        return refreshTokenCookie;
    }

    public AuthResponseDto refreshAccessToken(HttpServletRequest request) {
        String refreshToken = getCookieValue(request, "refresh_token");
        if (refreshToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is missing");
        }

        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                .filter(tokens -> !tokens.isRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or revoked refresh token"));

        UserInfoEntity userInfoEntity = refreshTokenEntity.getUser();
        Authentication authentication = createAuthenticationObject(userInfoEntity);
        log.info("REFRESH AUTH:{}", authentication);
        String newAccessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return AuthResponseDto.builder()
                .accessToken(newAccessToken)
                .accessTokenExpiry(15 * 60) // 15 minutes
                .userName(userInfoEntity.getUserName())
                .tokenType(TokenType.Bearer)
                .build();
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    private void saveUserRefreshToken(UserInfoEntity userInfoEntity, String refreshToken) {
        var refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userInfoEntity)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepo.save(refreshTokenEntity);
    }


    public Boolean logUserActivity(String username, String activityType, String IPaddress) {
        UserActivityEntity activity = new UserActivityEntity();
        Optional<UserInfoEntity> userInfo = userInfoRepo.findByEmailId(username);
        String name = userInfo.get().getUserName();
        List<String> UserNames = userActivityRepo.findListofUsernameByActivity(activityType);
        logFile.basicLogs(UserNames);
        if (UserNames.contains(name)) { // Add one more feild inside Activity entity to check the user based on E-mail and not on user_name....
//        logFile.basicLogs("User already logged-in !!!");
            Boolean condition = true;
            logFile.loginFailed(username, activityType, condition, IPaddress);

            return true;


        } else {
            Boolean condition = false;
            logFile.loginFailed(username, activityType, condition, IPaddress);
            activity.setUsername(name);
            activity.setActivityType(activityType);
            activity.setTimeStamp(LocalDateTime.now());
            userActivityRepo.save(activity);
            return false;

        }

    }

    private static Authentication createAuthenticationObject(UserInfoEntity userInfoEntity) {
        // Extract user details from UserDetailsEntity
        String username = userInfoEntity.getEmailId();
        log.info("Username:{}", username);
        String password = userInfoEntity.getPassword();
        String roles = userInfoEntity.getRoles().toString();

        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.split(",");
        List<GrantedAuthority> authorities = Arrays.stream(roleArray)
                .map(role -> new SimpleGrantedAuthority(role.trim()))
                .collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authObj = new UsernamePasswordAuthenticationToken(username, password, authorities);
        log.info("AuthenObj:{}", authObj);

        return authObj;
    }


    public String access_token;

    public void setTokenValue(AuthResponseDto responseDto) {
        access_token = responseDto.getAccessToken();
    }


    public boolean validateToken() {
        final String accessToken = this.access_token;
        logFile.basicLogs("Access_token : {}",access_token);
        DecodedJWT jwt = JWT.decode(accessToken);
        logFile.basicLogs("jwt decoded: {}", jwt);
        Date validTill = jwt.getExpiresAt();
        String header = jwt.getHeader();
        String role = jwt.getClaim("role").toString();
        System.out.println("The role from the access token is : "+role);
        logFile.basicLogs("validation of access token : {}", validTill);
        return validTill.before(new Date());
    }

    public boolean validateToken(String access_token) {
        DecodedJWT jwt = JWT.decode(access_token);
        logFile.basicLogs("jwt decoded: {}", jwt);
        Date validTill = jwt.getExpiresAt();
        logFile.basicLogs("validation of access token : {}", validTill);
        System.out.println("The payload of JWT is: "+jwt.getPayload());
        return validTill.before(new Date());
    }

    public List<String> menuItems() {
        List<String> items = new ArrayList<>();
        items.add("Dosa");
        items.add("Poori");
        items.add("Onion-Dosa");
        return items;
    }

    public void empDetails(EmpDto employee) {
        String email = employee.getEmpEmailId();
        String name = employee.getEmp_name();
        String role = employee.getEmpRole();
        String number = employee.getMobile();
        String salary = employee.getSalary();
        boolean isNew = employee.isNew();
        logFile.basicLogs("Employee emial_id: {}", email);

        EmployeeEntity emp = new EmployeeEntity();
        emp.setEmpEmailId(email);
        emp.setEmpName(name);
        emp.setEmpRoles(role);
        emp.setNew(isNew);
        emp.setSalary(salary);
        emp.setMobileNumber(number);
        empRepo.save(emp);
    }

    public void empDetails(EmpDto empDto, Optional<EmployeeEntity> employee) {
        if (employee.isPresent()) {
            EmployeeEntity employeeEntity = employee.get();
            employeeEntity.setMobileNumber(empDto.getMobile());
            employeeEntity.setEmpEmailId(empDto.getEmpEmailId());
            employeeEntity.setSalary(empDto.getSalary());
            employeeEntity.setEmpName(empDto.getEmp_name());
            employeeEntity.setEmpRoles(empDto.getEmpRole());
            empRepo.save(employeeEntity);

        } else {
            throw new EntityNotFoundException("Employee not found");
        }
    }

    public String getRole (){
        final String accessToken = this.access_token;
        DecodedJWT jwt = JWT.decode(accessToken);
        String role = jwt.getClaim("role").toString();
        System.out.println("The Role is: "+role);
        return role;
    }


    public FileUploadResponse upload(FileUploadRequest fileUploadRequest) {
        ImageEntity imgEntity = new ImageEntity();
        imgEntity.setImgName(fileUploadRequest.multipartFile().getOriginalFilename());
        imgEntity.setImgUrl("");
        imgEntity.setImgSize(fileUploadRequest.multipartFile().getSize());
        imageRepo.save(imgEntity);

        FileUploadResponse response = new FileUploadResponse(imgEntity.getId(),imgEntity.getImgName(), imgEntity.getImgUrl(), imgEntity.getImgSize() );
        return response;

    }

    public UserInfoEntity registerNewUser(UserInfoDto userInfoDto) {
        UserInfoEntity user = new UserInfoEntity();
        user.setUserName(userInfoDto.getName());
        user.setRoles(Role.USER);
        user.setPassword(passwordEncoder.encode(userInfoDto.getPass()));
        user.setEmailId(userInfoDto.getEmail());
        userInfoRepo.save(user);

        return user;
    }

    public List<String> createEventImageFile(String eventName, MultipartFile posterImage, MultipartFile bannerImage) throws IOException {


        String baseDir = "event-images";
        String posterDir = "poster-image";
        String bannerDir = "banner-image";

        String result = eventName.replaceAll("\\s+", ""); // Removes all whitespace
        Path posterImageDir = Paths.get(baseDir, posterDir, result);
        Path bannerImageDir = Paths.get(baseDir, bannerDir, result);

        Files.createDirectories(posterImageDir);
        Files.createDirectories(bannerImageDir);

        // Define file paths
        Path posterImagePath = posterImageDir.resolve(posterImage.getOriginalFilename());
        Path bannerImagePath = bannerImageDir.resolve(bannerImage.getOriginalFilename());

        // Write files
        Files.write(posterImagePath, posterImage.getBytes());
        Files.write(bannerImagePath, bannerImage.getBytes());
        return Arrays.asList(posterImagePath.toString(), bannerImagePath.toString());

    }

    public EventEntity addEventDetails(EventDto eventDto , String bannerImagePath , String posterImagePath ) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setEventName(eventDto.getEventName());
        eventEntity.setHostName(eventDto.getHostName());
        eventEntity.setMainDesc(eventDto.getMainDesc());
        eventEntity.setEmailId(eventDto.getHostEmailId());
        eventEntity.setSubDesc(eventDto.getSubDesc());
        eventEntity.setDate(eventDto.getDate());
        eventEntity.setTime(eventDto.getTime());
        eventEntity.setBannerImagePath(bannerImagePath);
        eventEntity.setPosterImagePath(posterImagePath);

        eventRepo.save(eventEntity);
        return eventEntity;

    }

    public EventEntity fetchEventDetails(String email) {
        Optional<EventEntity> optionalEventEntity =  eventRepo.findByEmailId(email);
        EventEntity eventEntity = optionalEventEntity.get();
        return eventEntity;
    }
}
