package com.ssafy.enjoytrip.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ssafy.enjoytrip.infrastructure.gson.adapter.LocalDateTimeAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class gsonConfig {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
}
