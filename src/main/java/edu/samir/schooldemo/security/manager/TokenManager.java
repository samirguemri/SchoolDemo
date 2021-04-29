package edu.samir.schooldemo.security.manager;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TokenManager {

    private Set<String> tokens = new HashSet<>();

    public void addToken(String token){
        this.tokens.add(token);
    }

    public boolean contains(String token){
        return this.tokens.contains(token);
    }

}
