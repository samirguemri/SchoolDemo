package edu.samir.schooldemo.controller;

import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.User;
import edu.samir.schooldemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdministratorRestController {

    private final UserService userService;

    @Autowired
    public AdministratorRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            path = "add",
            consumes = "application/json"
    )
    public ResponseEntity<User> addNewUser(@NotNull @RequestBody User administrator){
        if (administrator == null)
            return ResponseEntity.noContent().build();

        User newUser = userService.insertNewUser(administrator);

        String currentRequestPath = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
        String newPath = extractPath(currentRequestPath);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath(newPath)
                .path("/{id}")
                .buildAndExpand(newUser.getId())
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
    public ResponseEntity<User> selectStudent(@NotNull @PathVariable Long id) {
        User administrator = null;
        try {
            administrator = userService.selectUser(id);
        }catch (UserNotFoundException e){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(administrator);
    }

    @GetMapping(
            path = "all",
            produces = "application/json"
    )
    public ResponseEntity<List<User>> selectAllUsers(){
        List<User> administrators = userService.selectAllUsers();
        if (administrators.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(administrators);
    }

    @PutMapping(
            path = "{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<User> updateUser(@NotNull @PathVariable Long id, @NotNull @RequestBody User administrator) {
        User updatedUser = null;
        try {
            updatedUser = userService.updateUser(id, administrator);
        } catch (UserNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping(
            path = "{id}"
    )
    public ResponseEntity<Boolean> deleteUser(@NotNull @PathVariable Long id){
        try {
            userService.deleteUser(id);
        } catch (UserNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(true);
    }
 }