package edu.samir.schooldemo.service;

import edu.samir.schooldemo.repository.StudentRepository;
import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student insertNewStudent(@NotNull Student student){
        if ( studentRepository.findStudentByEmail(student.getEmail()).isPresent() ||
                        studentRepository.findStudentByUsername(student.getUsername()).isPresent() ) {
            throw new RuntimeException("Student with the given email OR username EXISTS");
        }
        return studentRepository.save(student);
    }

    public Student selectStudent(@NotNull Long id) throws UserNotFoundException {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            throw new UserNotFoundException("Student with the given Id does NOT EXISTS !");
        }
        return optionalStudent.get();
    }

    public List<Student> selectAllStudents(){
        return studentRepository.findAll();
    }

    public Student updateStudent(@NotNull Long id, Student student) throws UserNotFoundException {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            throw new UserNotFoundException("Student with the given Id does NOT EXISTS !");
        }
        Student studentFromRepository = optionalStudent.get();
        update(studentFromRepository, student);
        return studentRepository.save(studentFromRepository);
    }

    private void update(Student student, Student update) {
        student.setFirstName(update.getFirstName());
        student.setLastName(update.getLastName());
        student.setEmail(update.getEmail());
        student.setBirthday(update.getBirthday());
        student.setUsername(update.getUsername());
        student.setPassword(update.getPassword());
    }

    public void deleteStudent(@NotNull Long id) throws UserNotFoundException {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            throw new UserNotFoundException("Student with the given Id does NOT EXISTS !");
        }
        studentRepository.deleteById(id);
    }

    public void deleteStudent(@NotNull String username) throws UserNotFoundException {
        Optional<Student> optionalStudent = studentRepository.findStudentByUsername(username);
        Student student = optionalStudent.orElseThrow(() -> new UserNotFoundException("Student with the given Id does NOT EXISTS !"));
        studentRepository.deleteById(student.getId());
    }

}
