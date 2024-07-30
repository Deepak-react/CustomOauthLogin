package com.atquil.jwt_oauth2.logClass;

import com.atquil.jwt_oauth2.dto.*;
import com.atquil.jwt_oauth2.entity.*;
import com.atquil.jwt_oauth2.repo.*;
import com.auth0.jwt.interfaces.*;
import jakarta.servlet.http.*;
import lombok.*;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.*;


import java.util.*;

@RequiredArgsConstructor
@Component
public class LogFile  {
    private final UserInfoRepo userInfoRepo;
// Logger logger = LoggerFactory.getLogger(LogFile.class);
Logger logger = LogManager.getLogger(LogFile.class);


    public void loginFailed(String userName, String activityType, Boolean condition, String IPaddress){
        Optional<UserInfoEntity> userDetails  = userInfoRepo.findByEmailId(userName);
        if(condition){

            logger.error("User {} tried to {} already exists!!!, ",userName,activityType);
            logger.info("Login Status Failed");
            logger.debug("user details :{}",userDetails);
            logger.info("IP Address: {}", IPaddress);

        } else {
            logger.info("User {} {} is done!!!",userName,activityType);
            logger.info("Login status Successfull");
            logger.debug("user details :'{}'",userDetails);
            logger.info("IP Address: {}", IPaddress);
        }
    }
    public void cookieLog( Cookie refCookie,UserInfoEntity userInfo){
        logger.debug("Refresh Token created for : {} ",userInfo.getUserName());
        logger.info("Refresh token Stored in Cookie: {} ," +
                "Age of the Cookie: {}", refCookie.getValue(), refCookie.getMaxAge());
    }

    public void basicLogs(String logs){
        logger.info("userName: {}",logs);
    }
    public void basicLogs(List<String> logs) {
        logger.info("List User names: {}",logs);

    }

    public void basicLogs(String logs,String object){
        logger.info("userName: {} {}",logs,object);
    }
    public void basicLogs(String logs, DecodedJWT object){
        logger.info(" {} {}",logs,object);
    }

    public void basicLogs(String logs, Date object){
        logger.info(" {} {}",logs,object);
    }
    public void basicLogs(Boolean logs){
        logger.info("boolean value: {}",logs);
    }

    public void basicLogs(EmpDto empDto){
        logger.info("The Employee Payload is : {}",empDto);

    }
    public void basicLogs(EmployeeEntity employee){
        logger.info("The employee entity is : {}",employee);
    }


}
