package edu.samir.schooldemo.config;

import edu.samir.schooldemo.persistence.entity.Permission;
import edu.samir.schooldemo.persistence.entity.Role;
import edu.samir.schooldemo.persistence.entity.User;
import edu.samir.schooldemo.persistence.entity.enums.PermissionEnum;
import edu.samir.schooldemo.persistence.entity.enums.RoleEnum;
import edu.samir.schooldemo.persistence.repository.PermissionRepository;
import edu.samir.schooldemo.persistence.repository.RoleRepository;
import edu.samir.schooldemo.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.method.P;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository){
        return args -> {
            User sam = new User(
                    "Sam",
                    "Gomri",
                    "sam.gomri@gmail.com",
                    LocalDate.of(1996, 9, 02),
                    25,
                    "sam",
                    "12345"
            );

            User alex = new User(
                    "Alex",
                    "Dubois",
                    "alex.dubois@gmail.com",
                    LocalDate.of(1999, 3, 18),
                    22,
                    "alex",
                    "12345"
            );
            userRepository.saveAll(List.of(sam, alex));
            roleRepository.saveAll(List.of( new Role(RoleEnum.STUDENT),
                                            new Role(RoleEnum.ADMIN),
                                            new Role(RoleEnum.MANAGER),
                                            new Role(RoleEnum.TRAINEE)));
            permissionRepository.saveAll(List.of(   new Permission(PermissionEnum.STUDENT_READ),
                                                    new Permission(PermissionEnum.STUDENT_WRITE),
                                                    new Permission(PermissionEnum.ADMIN_READ),
                                                    new Permission(PermissionEnum.ADMIN_WRITE)));
        };
    }
}
