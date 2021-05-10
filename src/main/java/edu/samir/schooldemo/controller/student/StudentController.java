package edu.samir.schooldemo.controller.student;

import edu.samir.schooldemo.controller.dto.UserConverter;
import edu.samir.schooldemo.controller.dto.UserDto;
import edu.samir.schooldemo.exception.EmailNotValidException;
import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.service.UserService;
import edu.samir.schooldemo.service.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.util.List;

@AllArgsConstructor
@Controller
public class StudentController {

    private final UserConverter userConverter;
    private final UserService userService;
    private final RegistrationService registrationService;

    @GetMapping("/student/select/{studentId}")
    public String selectStudent(Model model, @PathVariable Long studentId) {
        UserEntity user;
        try {
            user = userService.selectUser(studentId);
        } catch (UserNotFoundException e) {
            return "error";
        }
        model.addAttribute("student",userConverter.entityToDto(user));
        return "student/student";
    }

    @GetMapping("/student/new")
    public String createStudentForm(Model model) {
        model.addAttribute("student", new UserDto());
        return "student/new";
    }

    @PostMapping("/student/add")
    public String addNewStudent(HttpServletRequest request, final @ModelAttribute("student") @NotNull UserDto student) throws EmailNotValidException, MalformedURLException {
        String url = this.getAppUrl(request);
        UserEntity userEntity = userConverter.dtoToEntity(student);
        registrationService.registerNewUser(url, userEntity);
        return "redirect:/manager/users/student";
    }

    @GetMapping("/manager/users/{role}")
    public String usersByRole(Model model,@PathVariable String role) {
        List<UserEntity> users = userService.selectUserByRole(role);
         model.addAttribute("students", userConverter.entitiesToDtos(users));
        return "student/student";
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort();
    }
}
