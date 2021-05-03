package edu.samir.schooldemo.service;

import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity selectUser(@NotNull Long id) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with the given Id does NOT EXISTS !");
        }
        return optionalUser.get();
    }

    public List<UserEntity> selectAllUsers(){
        return userRepository.findAll();
    }

    public UserEntity updateUser(@NotNull Long id, UserEntity userEntity) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with the given Id does NOT EXISTS !");
        }
        UserEntity userEntityFromRepository = optionalUser.get();
        this.update(userEntityFromRepository, userEntity);
        return userRepository.save(userEntityFromRepository);
    }

    private void update(UserEntity userEntity, UserEntity update) {
        userEntity.setFirstName(update.getFirstName());
        userEntity.setLastName(update.getLastName());
        userEntity.setEmail(update.getEmail());
        userEntity.setBirthday(update.getBirthday());
        userEntity.setUsername(update.getUsername());
        userEntity.setPassword(update.getPassword());
    }

    public void deleteUserById(@NotNull Long id) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with the given Id does NOT EXISTS !");
        }
        userRepository.deleteById(id);
    }

    public void deleteUserByUsername(@NotNull final String username) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findUserByUsername(username);
        UserEntity userEntity = optionalUser.orElseThrow(() -> new UserNotFoundException("User with the given Id does NOT EXISTS !"));
        userRepository.deleteById(userEntity.getId());
    }

}
