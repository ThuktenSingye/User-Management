package com.sdu.usermanagement.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sdu.usermanagement.model.User;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class LogService {
    public void logApplicationStatus(String action) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                // Assuming your User class has a method to get the user ID
                Integer userId = ((User) principal).getUserId();
                // You can log the user ID along with the action
                String logMessage = String.format("User ID '%d' performed action: %s", userId, action);
                // Log the message using your preferred logging framework (e.g., log4j, logback)
                log.info(logMessage);
            } else {
                // Handle the case where the principal is not an instance of User (if needed)
                log.warn("Unexpected principal type while trying to log an action.");
            }
        } else{
            // Handle the case where there is no authenticated user (if needed)
            log.warn("No authenticated user found while trying to log an action.");
        }
    }
}
