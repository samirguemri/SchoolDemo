package edu.samir.schooldemo.security;

import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.User;
import edu.samir.schooldemo.persistence.repository.UserRepository;
import edu.samir.schooldemo.security.model.MyUserDetails;
import edu.samir.schooldemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("MyUserDetailsManager")
public class MyUserDetailsManager implements UserDetailsManager {

    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        final Optional<User> optionalStudent = userRepository.findUserByUsername(username);
        User user = optionalStudent.orElseThrow(() -> new UsernameNotFoundException("Student with the given username does NOT EXISTS"));
        return new MyUserDetails(user);
    }

    @Override
    public void createUser(UserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        try {
            userService.updateUser(user.getId(),user);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(String username) {
        try {
            userService.deleteUser(username);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        }
        String username = currentUser.getName();
        User user = getUserFromUserDetails(loadUserByUsername(username));
        user.setPassword(newPassword);
    }

    @Override
    public boolean userExists(String username) {
        return loadUserByUsername(username) != null;
    }

    private User getUserFromUserDetails(UserDetails userDetails){
        MyUserDetails details = (MyUserDetails) userDetails;
        return details.getUser();
    }
}
