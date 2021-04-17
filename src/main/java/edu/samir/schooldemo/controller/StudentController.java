package edu.samir.schooldemo.controller;

import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.entities.Student;
import edu.samir.schooldemo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping(
            path = "add",
            consumes = "application/json"
    )
    public ResponseEntity<Student> addStudent(@NotNull @RequestBody Student student){
        if (student == null)
            return ResponseEntity.noContent().build();

        Student newStudent = studentService.insertNewStudent(student);

        String currentRequestPath = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
        String newPath = extractPath(currentRequestPath);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath(newPath)
                .path("/{id}")
                .buildAndExpand(newStudent.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    private String extractPath(String currentRequestPath) {
        int index = currentRequestPath.lastIndexOf("/");
        return currentRequestPath.substring(0, index);
    }


    @GetMapping(
            path = "{id}",
            produces = "application/json"
    )
    public ResponseEntity<Student> selectStudent(@NotNull @PathVariable Long id) {
        Student student = null;
        try {
            student = studentService.selectStudent(id);
        }catch (UserNotFoundException e){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping(
            path = "all",
            produces = "application/json"
    )
    public ResponseEntity<List<Student>> selectAllStudents(){
        List<Student> students = studentService.selectAllStudents();
        if (students.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }

    @PutMapping(
            path = "{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<Student> updateStudent(@NotNull @PathVariable Long id, @NotNull @RequestBody Student student) {
        Student updatedStudent = null;
        try {
            updatedStudent = studentService.updateStudent(id, student);
        } catch (UserNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping(
            path = "{id}"
    )
    public ResponseEntity<Boolean> deleteStudent(@NotNull @PathVariable Long id){
        try {
            studentService.deleteStudent(id);
        } catch (UserNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(true);
    }
 }
