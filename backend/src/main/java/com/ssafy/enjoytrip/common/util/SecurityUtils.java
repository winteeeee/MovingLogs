package com.ssafy.enjoytrip.common.util;

import com.ssafy.enjoytrip.exception.AuthenticationException;
import com.ssafy.enjoytrip.security.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

public class SecurityUtils {
    private SecurityUtils() {}

    public static Long getUserUidBySession(HttpSession session) {
        SecurityContext context = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        CustomUserDetails user = (CustomUserDetails) context.getAuthentication().getPrincipal();
        return Long.parseLong(user.getUser().getUid().getId());
    }

    public static String getUserUidBySecurityContextHolder() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new AuthenticationException();
        }

        return auth.getPrincipal().toString();
    }
}
