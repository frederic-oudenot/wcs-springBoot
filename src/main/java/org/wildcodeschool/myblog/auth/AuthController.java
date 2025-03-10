package org.wildcodeschool.myblog.auth;

import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.security.AuthenticationService;
import org.wildcodeschool.myblog.user.UserEntity;
import org.wildcodeschool.myblog.user.UserLoginDTO;
import org.wildcodeschool.myblog.user.UserRegistrationDTO;
import org.wildcodeschool.myblog.user.UserService;

import java.util.Set;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
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

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody UserLoginDTO userLoginDTO) {
        String token = authenticationService.authenticate(
                userLoginDTO.getEmail(),
                userLoginDTO.getPassword()
        );
        return ResponseEntity.ok(token);
    }
}
