package edu.samir.schooldemo.security;

import edu.samir.schooldemo.entities.Student;
import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.repository.StudentRepository;
import edu.samir.schooldemo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MyJpaUserDetailsManager implements UserDetailsManager {

    @Autowired private StudentRepository studentRepository;
    @Autowired private StudentService studentService;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        final Optional<Student> optionalStudent = studentRepository.findStudentByUsername(username);
        Student student = optionalStudent.orElseThrow(() -> new UsernameNotFoundException("Student with the given username does NOT EXISTS"));
        return new StudentUserDetails(student);
    }

    private Student getStudentFromStudentDetails(UserDetails userDetails){
        StudentUserDetails studentUserDetails = (StudentUserDetails) userDetails;
        return studentUserDetails.getStudent();
    }

    @Override
    public void createUser(UserDetails userDetails) {
        Student student = getStudentFromStudentDetails(userDetails);
        studentRepository.save(student);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        Student student = getStudentFromStudentDetails(userDetails);
        try {
            studentService.updateStudent(student.getId(),student);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(String username) {
        try {
            studentService.deleteStudent(username);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        }
        String username = currentUser.getName();
        Student student = getStudentFromStudentDetails(loadUserByUsername(username));
        student.setPassword(newPassword);
    }

    @Override
    public boolean userExists(String username) {
        return loadUserByUsername(username) != null;
    }

}
