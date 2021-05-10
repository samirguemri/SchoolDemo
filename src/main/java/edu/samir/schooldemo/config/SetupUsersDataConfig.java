package edu.samir.schooldemo.config;

import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.persistence.entity.UserPermission;
import edu.samir.schooldemo.persistence.entity.UserRole;
import edu.samir.schooldemo.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static edu.samir.schooldemo.persistence.entity.enums.Permission.*;
import static edu.samir.schooldemo.persistence.entity.enums.Role.*;

@AllArgsConstructor
@Configuration
public class SetupUsersDataConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner commandLineRunner(){
        return args -> {
//            this.setupUsersData();
        };
    }

    @Transactional
    public void setupUsersData(){

        UserPermission studentRead = new UserPermission(STUDENT_READ);
        UserPermission studentWrite = new UserPermission(STUDENT_WRITE);
        UserPermission courseRead = new UserPermission(COURSE_READ);
        UserPermission courseWrite = new UserPermission(COURSE_WRITE);
        UserPermission adminRead = new UserPermission(ADMIN_READ);
        UserPermission adminWrite = new UserPermission(ADMIN_WRITE);

        UserRole studentRole = new UserRole(ROLE_STUDENT);
        studentRole.setPermissions(List.of(studentRead, studentWrite));

        UserRole teacherRole = new UserRole(ROLE_TEACHER);
        teacherRole.setPermissions(List.of(courseRead, courseWrite, studentRead));

        UserRole adminRole = new UserRole(ROLE_ADMIN);
        adminRole.setPermissions(List.of(adminRead, courseRead, studentRead, studentWrite));

        UserRole managerRole = new UserRole(ROLE_MANAGER);
        managerRole.setPermissions(List.of(adminRead, adminWrite, courseRead, studentRead, studentWrite));

        UserEntity jean = new UserEntity(
                "Jean",
                "Dubois",
                "jean.dubois@gmail.com",
                LocalDate.of(1999, 3, 15),
                22,
                "jean",
                passwordEncoder.encode("12345")
        );
        jean.setUserRoles(List.of(studentRole));
        jean.setEnabled(true);

        UserEntity raoul = new UserEntity(
                "Raoul",
                "Martin",
                "raoul.martin@gmail.com",
                LocalDate.of(2000, 8, 25),
                21,
                "raoul",
                passwordEncoder.encode("12345")
        );
        raoul.setUserRoles(List.of(studentRole));
        raoul.setEnabled(true);

        UserEntity frank = new UserEntity(
                "Frank",
                "Macs",
                "frank.macs@gmail.com",
                LocalDate.of(1990, 9, 23),
                31,
                "frank",
                passwordEncoder.encode("12345")
        );
        frank.setUserRoles(List.of(teacherRole));
        frank.setEnabled(true);

        UserEntity najat = new UserEntity(
                "Najat",
                "Matri",
                "najat.matri@gmail.com",
                LocalDate.of(1992, 10, 02),
                29,
                "najat",
                passwordEncoder.encode("12345")
        );
        najat.setUserRoles(List.of(adminRole, teacherRole));
        najat.setEnabled(true);

        UserEntity sam = new UserEntity(
                "Sam",
                "Garri",
                "sam.garri@gmail.com",
                LocalDate.of(1985, 7, 22),
                36,
                "sam",
                passwordEncoder.encode("12345")
        );
        sam.setUserRoles(List.of(adminRole));
        sam.setEnabled(true);

        UserEntity alicia = new UserEntity(
                "Alicia",
            "Ahmed",
                "alicia.ahmed@gmail.com",
                LocalDate.of(1970, 5, 28),
                51,
                "alicia",
                passwordEncoder.encode("12345")
        );
        alicia.setUserRoles(List.of(managerRole));
        alicia.setEnabled(true);

        userRepository.saveAll(List.of(jean, raoul, frank, najat, sam, alicia));
    }
}
