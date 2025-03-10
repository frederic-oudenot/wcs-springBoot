package org.wildcodeschool.myblog.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.exception.RessourceExistsException;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity registerUser(String email, String password, Set<String> roles) {
        if(userRepository.existsByEmail(email)) {
            throw new RessourceExistsException("User with email " + email + " already exists");
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public UserEntity getUserProfile(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User with id " + id + " not found"));
        return user;
    }

    public UserEntity updateUserProfile(Long id, UserUpdateDTO user) {
        UserEntity foundUser = this.getUserProfile(id);
        foundUser.setPassword(user.getPassword());
        return userRepository.save(foundUser);
    }

    public void deleteUserProfile(Long id) {
        UserEntity user = this.getUserProfile(id);
        userRepository.delete(user);
    }

}
