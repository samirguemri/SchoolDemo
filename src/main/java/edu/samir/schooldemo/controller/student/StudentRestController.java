package edu.samir.schooldemo.controller.student;

import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
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
    public ResponseEntity<UserEntity> selectUser(@NotNull @PathVariable Long id) {
        UserEntity userEntity = null;
        try {
            userEntity = userService.selectUser(id);
        }catch (UserNotFoundException e){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userEntity);
    }

    @GetMapping(
            path = "/api/student/all",
            produces = "application/json"
    )
    public ResponseEntity<List<UserEntity>> selectAllStudents(){
        List<UserEntity> students = userService.selectAllUsers();
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
    public ResponseEntity<UserEntity> updateStudent(@NotNull @PathVariable Long id, @NotNull @RequestBody UserEntity student) {
        UserEntity updatedStudent = null;
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
