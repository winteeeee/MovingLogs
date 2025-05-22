package com.ssafy.enjoytrip.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private JwtUtils() {}

    public static void writeJwtTokensToResponse(HttpServletResponse response, String accessToken, String refreshToken) {
        Map<String, String> result = new HashMap<>();
        result.put("status", "SUCCESS");
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        handleResult(response, result, HttpStatus.OK);
    }

    private static void handleResult(HttpServletResponse response, Map<String, String> data, HttpStatus status) {
        response.setContentType("application/json;charset=UTF-8");
        try {
            String jsonResponse = new ObjectMapper().writeValueAsString(data);
            response.setStatus(status.value());
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
