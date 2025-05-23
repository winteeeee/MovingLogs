package com.ssafy.enjoytrip.common.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtUtils {
    private JwtUtils() {}

    public static void redirectWithJwtToken(HttpServletResponse response, String accessToken) throws IOException {
        response.sendRedirect("http://localhost:5173/login/success?token=" + accessToken);
    }
}
