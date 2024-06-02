package org.swp.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

public class SecurityUtil {
    public static String getUserName(SecurityContext securityContext) {
        Authentication authentication = securityContext.getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return Objects.nonNull(username) ? username : null;
    }
}
