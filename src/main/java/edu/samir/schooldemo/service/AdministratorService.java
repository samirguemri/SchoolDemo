package edu.samir.schooldemo.service;

import edu.samir.schooldemo.repository.AdministratorRepository;
import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.entities.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class AdministratorService {

    private final AdministratorRepository administratorRepository;

    @Autowired
    public AdministratorService(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    public Administrator insertNewAdministrator(@NotNull Administrator administrator){
        if ( administratorRepository.findAdministratorByEmail(administrator.getEmail()).isPresent() ||
                administratorRepository.findAdministratorByUsername(administrator.getUsername()).isPresent() ) {
            throw new RuntimeException("Student with the given email OR username EXISTS");
        }
        return administratorRepository.save(administrator);
    }

    public Administrator selectAdministrator(@NotNull Long id) throws UserNotFoundException {
        Optional<Administrator> optionalAdministrator = administratorRepository.findById(id);
        if (optionalAdministrator.isEmpty()) {
            throw new UserNotFoundException("Administrator with the given Id does NOT EXISTS !");
        }
        return optionalAdministrator.get();
    }

    public List<Administrator> selectAllAdministrators(){
        return administratorRepository.findAll();
    }

    public Administrator updateAdministrator(@NotNull Long id, Administrator administrator) throws UserNotFoundException {
        Optional<Administrator> optionalAdministrator = administratorRepository.findById(id);
        if (optionalAdministrator.isEmpty()) {
            throw new UserNotFoundException("Administrator with the given Id does NOT EXISTS !");
        }
        Administrator administratorFromRepository = optionalAdministrator.get();
        update(administratorFromRepository,administrator);
        return administratorRepository.save(administratorFromRepository);
    }

    private void update(Administrator administrator, Administrator update) {
        administrator.setFirstName(update.getFirstName());
        administrator.setLastName(update.getLastName());
        administrator.setBirthday(update.getBirthday());
        administrator.setPassword(update.getPassword());
    }

    public void deleteAdministrator(@NotNull Long id) throws UserNotFoundException {
        Optional<Administrator> optionalStudent = administratorRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            throw new UserNotFoundException("Administrator with the given Id does NOT EXISTS !");
        }
        administratorRepository.deleteById(id);
    }
}
