package com.example.hotel_booking_api.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityCurrentUserProvider implements CurrentUserProvider {

    @Override
    public String getCurrentUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        return auth.getName();
    }
}
