package edu.samir.schooldemo.service;

import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

public interface UserService {
    UserEntity selectUser(@NotNull Long id) throws UserNotFoundException;

    List<UserEntity> selectUserByRole(@NotNull String userRole);

    List<UserEntity> selectAllUsers();

    UserEntity updateUser(@NotNull Long id, UserEntity userEntity) throws UserNotFoundException;

    void deleteUserById(@NotNull Long id) throws UserNotFoundException;

    void deleteUserByUsername(@NotNull String username) throws UserNotFoundException;
}
