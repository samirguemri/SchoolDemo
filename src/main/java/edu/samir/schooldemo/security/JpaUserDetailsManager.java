package edu.samir.schooldemo.security;

import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.User;
import edu.samir.schooldemo.persistence.repository.UserRepository;
import edu.samir.schooldemo.security.model.SecurityUser;
import edu.samir.schooldemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("MyUserDetailsManager")
public class JpaUserDetailsManager implements UserDetailsManager {

    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> optionalStudent = userRepository.findUserByUsername(username);
        User user = optionalStudent.orElseThrow(() -> new UsernameNotFoundException("User with the given username does NOT EXISTS"));
        return new SecurityUser(user);
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
            userService.deleteUserByUsername(username);
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
        try {
            loadUserByUsername(username);
            return true;
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    private User getUserFromUserDetails(UserDetails userDetails){
        SecurityUser securityUser = (SecurityUser) userDetails;
        return securityUser.getUser();
    }
}
