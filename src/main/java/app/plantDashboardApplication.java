package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"controllers", "service", "simulator", "timetable"})
@EntityScan("model.entity")
@EnableJpaRepositories("repo")
@EnableScheduling
public class plantDashboardApplication {
    public static void main(String[] args) {
        SpringApplication.run(plantDashboardApplication.class, args);
    }
}