package edu.samir.schooldemo.service;

import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.persistence.entity.enums.Role;
import edu.samir.schooldemo.persistence.repository.ConfirmationTokenRepository;
import edu.samir.schooldemo.persistence.repository.UserRepository;
import edu.samir.schooldemo.service.registration.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository tokenRepository;

    @Override
    public UserEntity selectUser(@NotNull Long id) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with the given Id does NOT EXISTS !");
        }
        return optionalUser.get();
    }

    @Override
    @Transactional
    public List<UserEntity> selectUserByRole(@NotNull String roleName) {
        Role role = Role.valueOf("ROLE_" + roleName.toUpperCase());
        return userRepository.findByRole(role);
    }

    @Override
    public List<UserEntity> selectAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public UserEntity updateUser(@NotNull Long id, UserEntity updateEntity) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with the given Id does NOT EXISTS !");
        }
        UserEntity entityToUpdate = optionalUser.get();
        this.update(entityToUpdate, updateEntity);
        return userRepository.save(entityToUpdate);
    }

    private void update(UserEntity entityToUpdate, UserEntity update) {
        entityToUpdate.setFirstName(update.getFirstName());
        entityToUpdate.setLastName(update.getLastName());
        entityToUpdate.setEmail(update.getEmail());
        entityToUpdate.setBirthday(update.getBirthday());
        entityToUpdate.setUsername(update.getUsername());
        entityToUpdate.setPassword(update.getPassword());
    }

    @Override
    @Transactional
    public void deleteUserById(@NotNull Long id) throws UserNotFoundException, IllegalArgumentException {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        UserEntity user = optionalUser.orElseThrow(
                () -> new UserNotFoundException("User with the given Id does NOT EXISTS !")
        );
        this.deleteConfirmationToken(user);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void deleteUserByUsername(@NotNull final String username) throws UserNotFoundException, IllegalArgumentException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        UserEntity user = optionalUser.orElseThrow(
                () -> new UserNotFoundException("User with the given Id does NOT EXISTS !")
        );
        this.deleteConfirmationToken(user);
        userRepository.delete(user);
    }

    private void deleteConfirmationToken(UserEntity user) throws IllegalArgumentException {
        Optional<ConfirmationToken> token = tokenRepository.findByUser(user);
        if (token.isPresent()){
            tokenRepository.delete(token.get());
        }
    }
}
