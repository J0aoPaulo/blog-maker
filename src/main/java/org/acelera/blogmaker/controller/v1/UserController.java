package org.acelera.blogmaker.controller.v1;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.acelera.blogmaker.model.Role;
import org.acelera.blogmaker.model.dto.request.CreateUserRequest;
import org.acelera.blogmaker.model.dto.request.UpdateUserRequest;
import org.acelera.blogmaker.model.dto.response.UserResponse;
import org.acelera.blogmaker.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest request) {
        UUID userId = userService.createUser(request, Role.USER);
        return ResponseEntity.created(URI.create("/api/v1/users/" + userId)).build();
    }

    @PutMapping("/{userId}")
    @Transactional
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId,
                                                   @Valid @RequestBody UpdateUserRequest request) {
        UserResponse updatedUser = userService.updateUser(userId, request);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
