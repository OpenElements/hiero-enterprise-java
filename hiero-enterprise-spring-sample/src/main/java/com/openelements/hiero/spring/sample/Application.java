package com.openelements.hiero.spring.sample;

import com.openelements.hiero.spring.EnableHiero;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableHiero
public class Application {

    @Autowired
    MeterRegistry registry;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
