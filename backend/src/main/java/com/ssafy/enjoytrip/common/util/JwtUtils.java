package com.ssafy.enjoytrip.common.util;

import com.ssafy.enjoytrip.config.WebConfig;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class JwtUtils {
    private JwtUtils() {}

    public static void redirectWithJwtToken(HttpServletResponse response, String accessToken, String name) throws IOException {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        response.sendRedirect( WebConfig.FRONTEND_URL + "/login/success?token=" + accessToken + "&name=" + encodedName);
    }
}
