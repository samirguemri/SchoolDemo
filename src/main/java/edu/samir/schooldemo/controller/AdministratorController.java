package edu.samir.schooldemo.controller;

import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.entities.Administrator;
import edu.samir.schooldemo.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdministratorController {

    private final AdministratorService administratorService;

    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @PostMapping(
            path = "add",
            consumes = "application/json"
    )
    public ResponseEntity<Administrator> addAdministrator(@NotNull @RequestBody Administrator administrator){
        if (administrator == null)
            return ResponseEntity.noContent().build();

        Administrator newAdministrator = administratorService.insertNewAdministrator(administrator);

        String currentRequestPath = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
        String newPath = extractPath(currentRequestPath);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath(newPath)
                .path("/{id}")
                .buildAndExpand(newAdministrator.getId())
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
    public ResponseEntity<Administrator> selectStudent(@NotNull @PathVariable Long id) {
        Administrator administrator = null;
        try {
            administrator = administratorService.selectAdministrator(id);
        }catch (UserNotFoundException e){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(administrator);
    }

    @GetMapping(
            path = "all",
            produces = "application/json"
    )
    public ResponseEntity<List<Administrator>> selectAllAdministrators(){
        List<Administrator> administrators = administratorService.selectAllAdministrators();
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
    public ResponseEntity<Administrator> updateAdministrator(@NotNull @PathVariable Long id, @NotNull @RequestBody Administrator administrator) {
        Administrator updatedAdministrator = null;
        try {
            updatedAdministrator = administratorService.updateAdministrator(id, administrator);
        } catch (UserNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(updatedAdministrator);
    }

    @DeleteMapping(
            path = "{id}"
    )
    public ResponseEntity<Boolean> deleteAdministrator(@NotNull @PathVariable Long id){
        try {
            administratorService.deleteAdministrator(id);
        } catch (UserNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(true);
    }
 }
