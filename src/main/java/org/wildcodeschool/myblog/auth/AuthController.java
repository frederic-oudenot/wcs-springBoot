package org.wildcodeschool.myblog.auth;

import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.user.UserEntity;
import org.wildcodeschool.myblog.user.UserRegistrationDTO;
import org.wildcodeschool.myblog.user.UserService;

import java.util.Set;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserEntity registeredUser = userService.registerUser(
                userRegistrationDTO.getEmail(),
                userRegistrationDTO.getPassword(),
                Set.of("ROLE_USER")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
}
