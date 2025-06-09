package com.ssafy.enjoytrip.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.config.WebConfig;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    @Value("${server.allowed-origin}")
    public String allowedOrigin;

    private JwtUtils() {}

    public void redirectWithJwtToken(HttpServletResponse response, String accessToken, String name) throws IOException {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        response.sendRedirect( allowedOrigin + "/login/success?token=" + accessToken + "&name=" + encodedName);
    }

    public void responseWithJwtToken(HttpServletResponse response, String accessToken, String name) throws IOException {
        Map<String, String> result = new HashMap<>();
        result.put("status", "accessToken");
        result.put("accessToken", accessToken);
        result.put("name", name);

        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse = new ObjectMapper().writeValueAsString(result);
        response.getWriter().write(jsonResponse);
    }
}
