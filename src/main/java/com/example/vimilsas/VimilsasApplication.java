package com.example.vimilsas;


import com.example.vimilsas.controller.ArticleCLIController;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.Scanner;

@SpringBootApplication
public class VimilsasApplication {

    public static void main(String[] args) {
        SpringApplication.run(VimilsasApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ArticleCLIController cliController) {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            cliController.start(scanner);
        };
    }
}
