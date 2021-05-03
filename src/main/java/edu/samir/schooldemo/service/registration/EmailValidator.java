package edu.samir.schooldemo.service.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@AllArgsConstructor
@Service
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String email) {
        // TODO : regex to validate email
        return true;
    }
}
