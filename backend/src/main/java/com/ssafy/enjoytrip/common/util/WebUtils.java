package com.ssafy.enjoytrip.common.util;

import jakarta.servlet.http.HttpServletRequest;

public class WebUtils {
    private WebUtils() {}

    public static String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }
}
