package edu.samir.schooldemo.service;

import edu.samir.schooldemo.controller.dto.UserDto;
import edu.samir.schooldemo.persistence.repository.UserRepository;
import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerNewUser(@NotNull final UserDto userDto){

        if ( userRepository.findUserByEmail(userDto.getEmail()).isPresent() ||
                userRepository.findUserByUsername(userDto.getUsername()).isPresent() ) {
            throw new RuntimeException("User with the given email OR username EXISTS");
        }

    }

    public User insertNewUser(@NotNull final User user){

/*
        if ( userRepository.findUserByEmail(user.getEmail()).isPresent() ||
                        userRepository.findUserByUsername(user.getUsername()).isPresent() ) {
            throw new RuntimeException("User with the given email OR username EXISTS");
        }
*/
        return userRepository.save(user);
    }

    public User selectUser(@NotNull Long id) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with the given Id does NOT EXISTS !");
        }
        return optionalUser.get();
    }

    public List<User> selectAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(@NotNull Long id, User user) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with the given Id does NOT EXISTS !");
        }
        User userFromRepository = optionalUser.get();
        update(userFromRepository, user);
        return userRepository.save(userFromRepository);
    }

    private void update(User user, User update) {
        user.setFirstName(update.getFirstName());
        user.setLastName(update.getLastName());
        user.setEmail(update.getEmail());
        user.setBirthday(update.getBirthday());
        user.setUsername(update.getUsername());
        user.setPassword(update.getPassword());
    }

    public void deleteUser(@NotNull Long id) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with the given Id does NOT EXISTS !");
        }
        userRepository.deleteById(id);
    }

    public void deleteUser(@NotNull final String username) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException("User with the given Id does NOT EXISTS !"));
        userRepository.deleteById(user.getId());
    }

}
