package org.wildcodeschool.myblog.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("profile")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("id == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<UserEntity> getUserProfile(@PathVariable Long id) {
        UserEntity user = userService.getUserProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("id == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<UserEntity> updateProfile(@Valid @RequestBody UserUpdateDTO userUpdateDTO, @PathVariable Long id) {
        UserEntity user = userService.updateUserProfile(id, userUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserEntity> deleteProfile(@PathVariable Long id) {
        userService.deleteUserProfile(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
