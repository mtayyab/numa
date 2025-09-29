package com.numa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main application class for the Numa multi-tenant restaurant ordering platform.
 * 
 * This application provides:
 * - Multi-tenant restaurant management
 * - QR-based menu ordering system
 * - Group ordering and bill splitting
 * - Real-time order management
 * - Analytics and reporting
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableTransactionManagement
public class NumaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NumaApplication.class, args);
    }
}
