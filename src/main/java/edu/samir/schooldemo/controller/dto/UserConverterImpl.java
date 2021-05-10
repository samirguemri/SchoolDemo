package edu.samir.schooldemo.controller.dto;

import edu.samir.schooldemo.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class UserConverterImpl implements UserConverter {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto entityToDto(UserEntity entity){
        UserDto dto = new UserDto();

        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setBirthday(entity.getBirthday());
        dto.setUsername(entity.getUsername());
        dto.setPassword("*****");

        return dto;
    }

    @Override
    public List<UserDto> entitiesToDtos(List<UserEntity> userEntityList) {
        List<UserDto> dtos = new ArrayList<>();
        for (UserEntity entity : userEntityList) {
            dtos.add(this.entityToDto(entity));
        }
        return dtos;
    }

    @Override
    public UserEntity dtoToEntity(final UserDto userDto) {

        UserEntity userEntity = new UserEntity();

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setBirthday(userDto.getBirthday());
        userEntity.setAge(LocalDate.now().getYear() - userDto.getBirthday().getYear());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setEnabled(false);

        return userEntity;
    }

}
