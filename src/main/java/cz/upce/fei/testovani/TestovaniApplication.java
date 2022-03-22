package cz.upce.fei.testovani;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class TestovaniApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestovaniApplication.class, args);
    }

}
