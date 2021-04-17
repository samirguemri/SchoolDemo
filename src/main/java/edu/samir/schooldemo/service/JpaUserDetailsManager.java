package edu.samir.schooldemo.service;

import edu.samir.schooldemo.entities.Student;
import edu.samir.schooldemo.repository.StudentRepository;
import edu.samir.schooldemo.security.SecurityStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class JpaUserDetailsManager implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        final Optional<Student> optionalStudent = studentRepository.findStudentByUsername(username);
        Student student = optionalStudent.orElseThrow(() -> new UsernameNotFoundException("Student with the given username does NOT EXISTS"));
        return new SecurityStudent(student);
    }
}
