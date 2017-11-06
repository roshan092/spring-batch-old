package com.roshan092.springbatchdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfiguration {
    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Australia/Sydney"));
    }
}
