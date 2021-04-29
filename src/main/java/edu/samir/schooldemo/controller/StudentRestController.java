package edu.samir.schooldemo.controller;

import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.User;
import edu.samir.schooldemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping
public class StudentRestController {

    private final UserService userService;

    @Autowired
    public StudentRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(
            path = "/api/student/{id}",
            produces = "application/json"
    )
    public ResponseEntity<User> selectUser(@NotNull @PathVariable Long id) {
        User user = null;
        try {
            user = userService.selectUser(id);
        }catch (UserNotFoundException e){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping(
            path = "/api/student/all",
            produces = "application/json"
    )
    public ResponseEntity<List<User>> selectAllStudents(){
        List<User> students = userService.selectAllUsers();
        if (students.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }

    @PutMapping(
            path = "/api/student/{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<User> updateStudent(@NotNull @PathVariable Long id, @NotNull @RequestBody User student) {
        User updatedStudent = null;
        try {
            updatedStudent = userService.updateUser(id, student);
        } catch (UserNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping(
            path = "/api/student/{id}"
    )
    public ResponseEntity<Boolean> deleteStudent(@NotNull @PathVariable Long id){
        try {
            userService.deleteUserById(id);
        } catch (UserNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(true);
    }
 }
