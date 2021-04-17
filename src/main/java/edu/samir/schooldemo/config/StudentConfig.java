package edu.samir.schooldemo.config;

import edu.samir.schooldemo.repository.StudentRepository;
import edu.samir.schooldemo.entities.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository){
        return args -> {
            Student samir = new Student(
                    "Samir",
                    "Guemri",
                    "samir.guemri@gmail.com",
                    LocalDate.of(1986, 9, 22),
                    "samir2nice",
                    "test123"
            );
            studentRepository.saveAll(List.of(samir));
        };
    }
}
