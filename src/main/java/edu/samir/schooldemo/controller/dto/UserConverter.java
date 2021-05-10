package edu.samir.schooldemo.controller.dto;

import edu.samir.schooldemo.persistence.entity.UserEntity;

import java.util.List;

public interface UserConverter {

    UserDto entityToDto(UserEntity entity);

    List<UserDto> entitiesToDtos(List<UserEntity> userDtoList);

    UserEntity dtoToEntity(UserDto userDto);
}
