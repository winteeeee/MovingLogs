package com.ssafy.enjoytrip.common.util;

import com.ssafy.enjoytrip.config.WebConfig;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtUtils {
    private JwtUtils() {}

    public static void redirectWithJwtToken(HttpServletResponse response, String accessToken) throws IOException {
        response.sendRedirect( WebConfig.FRONTEND_URL + "/login/success?token=" + accessToken);
    }
}
