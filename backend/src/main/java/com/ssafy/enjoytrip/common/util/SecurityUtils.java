package com.ssafy.enjoytrip.common.util;

import com.ssafy.enjoytrip.exception.AuthenticationException;
import com.ssafy.enjoytrip.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    private SecurityUtils() {}

    public static String getUserUidBySecurityContextHolder() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new AuthenticationException();
        }

        return ((CustomUserDetails) auth.getPrincipal()).getUser().getUid().getId();
    }
}
