package edu.samir.schooldemo.security.userdetail;

import edu.samir.schooldemo.exception.UserNotFoundException;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import edu.samir.schooldemo.persistence.repository.UserRepository;
import edu.samir.schooldemo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CustomUserDetailsManager implements UserDetailsManager {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<UserEntity> optionalStudent = userRepository.findByUsername(username);
        UserEntity userEntity = optionalStudent.orElseThrow(() -> new UsernameNotFoundException("User with the given username does NOT EXISTS"));
        return new CustomUserDetails(userEntity);
    }

    @Override
    public void createUser(UserDetails userDetails) {

        UserEntity userEntity = getUserFromUserDetails(userDetails);

        String username = userEntity.getUsername();
        if ( userExists(username) )
            throw new IllegalArgumentException(String.format("username %s already taken !",username));

        String email = userEntity.getEmail();
        if ( emailExists(email) )
            throw new IllegalArgumentException(String.format("email %s already taken !",email));

        userRepository.save(userEntity);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        UserEntity userEntity = getUserFromUserDetails(userDetails);
        try {
            userService.updateUser(userEntity.getId(), userEntity);
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
        UserDetails userDetails = loadUserByUsername(username);
        UserEntity userEntity = getUserFromUserDetails(userDetails);
        userEntity.setPassword(newPassword);
        userRepository.save(userEntity);
    }

    @Override
    public boolean userExists(String username) {
            return userRepository.findByUsername(username).isPresent();
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private UserEntity getUserFromUserDetails(UserDetails userDetails){
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        return customUserDetails.getUserEntity();
    }

    public void enableUser(UserEntity userEntity) throws UserNotFoundException {
        userEntity.setEnabled(true);
        userService.updateUser(userEntity.getId(), userEntity);
    }
}
