package edu.samir.schooldemo.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.samir.schooldemo.config.converter.json.UserDtoDeserializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level= AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonDeserialize(using = UserDtoDeserializer.class)
public class UserDto {
    String firstName;
    String lastName;
    String email;
    LocalDate birthday;
    String username;
    String password;
}
