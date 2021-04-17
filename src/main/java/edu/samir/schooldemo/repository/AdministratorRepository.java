package edu.samir.schooldemo.repository;

import edu.samir.schooldemo.entities.Administrator;
import edu.samir.schooldemo.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator,Long> {
    Optional<Student> findAdministratorByEmail(String email);
    Optional<Student> findAdministratorByUsername(String username);
}
