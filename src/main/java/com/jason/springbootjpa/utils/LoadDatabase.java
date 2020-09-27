package com.jason.springbootjpa.utils;

import com.jason.springbootjpa.entity.AdminRole;
import com.jason.springbootjpa.repository.AdminRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(AdminRoleRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new AdminRole(1L, "ADMIN")));
        };
    }
}