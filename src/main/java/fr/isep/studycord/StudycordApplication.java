package fr.isep.studycord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Studycord Spring Boot application.
 *
 */
@SpringBootApplication
public class StudycordApplication {

    /**
     * Bootstraps and launches the Studycord application.
     *
     * @param args command-line arguments passed to the Spring context
     */
    public static void main(String[] args) {
        SpringApplication.run(StudycordApplication.class, args);
    }

}
