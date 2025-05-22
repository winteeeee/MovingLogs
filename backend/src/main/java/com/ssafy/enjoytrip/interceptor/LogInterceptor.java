package com.ssafy.enjoytrip.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        MDC.put("ip", getClientIp(request));
        MDC.put("requestTime", requestTime);
        log.info("({}) {} [{}]: Request", request.getMethod(), request.getRequestURI(), MDC.get("ip"));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LocalDateTime requestTime = LocalDateTime.parse(MDC.get("requestTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Duration responseDuration = Duration.between(requestTime, LocalDateTime.now());
        log.info("({}) {} [{}]: Response({} seconds)", request.getMethod(), request.getRequestURI(), MDC.get("ip"), responseDuration.getSeconds());
        MDC.clear();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = null;
        String[] headers = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        for (String header : headers) {
            ip = request.getHeader(header);
            if (ip != null) break;
        }

        if (ip == null) ip = request.getRemoteAddr();
        return ip;
    }
}
