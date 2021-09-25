package com.github.jitwxs.datakeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author jitwxs
 * @date 2021年04月04日 19:28
 */
@EnableScheduling
@SpringBootApplication
public class DatakeeperExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(DatakeeperExampleApplication.class);
    }
}
