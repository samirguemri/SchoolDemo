package edu.samir.schooldemo.config;

import edu.samir.schooldemo.persistence.entity.User;
import edu.samir.schooldemo.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository){
        return args -> {
            User samir = new User(
                    "Samir",
                    "Guemri",
                    "samir.guemri@gmail.com",
                    LocalDate.of(1986, 9, 22),
                    35,
                    "samir2nice",
                    "test123"
            );

            User alex = new User(
                    "Alex",
                    "Dubois",
                    "Alex.Dubois@gmail.com",
                    LocalDate.of(1999, 3, 18),
                    22,
                    "alex",
                    "test123"
            );
            userRepository.saveAll(List.of(samir, alex));
        };
    }
}
