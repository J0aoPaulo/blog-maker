package org.acelera.blogmaker.service.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.acelera.blogmaker.exception.UserAlreadyExistException;
import org.acelera.blogmaker.exception.UserNotFoundException;
import org.acelera.blogmaker.model.Role;
import org.acelera.blogmaker.model.User;
import org.acelera.blogmaker.model.dto.request.CreateUserRequest;
import org.acelera.blogmaker.model.dto.request.UpdateUserRequest;
import org.acelera.blogmaker.model.dto.response.UserResponse;
import org.acelera.blogmaker.repository.UserRepository;
import org.acelera.blogmaker.services.UserService;
import org.acelera.blogmaker.services.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.List;


class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldThrowUserAlreadyExistException_WhenEmailExists() {
        CreateUserRequest request = new CreateUserRequest("Name", "email@example.com", "pass", null);
        when(repository.existsByEmail("email@example.com")).thenReturn(true);

        UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class, () ->
                userService.createUser(request, Role.USER)
        );
        assertTrue(exception.getMessage().contains("User already exist"));
    }

    @Test
    void createUser_ShouldSaveUser_WhenEmailDoesNotExist() {
        CreateUserRequest request = new CreateUserRequest("Name", "new@example.com", "pass", null);
        when(repository.existsByEmail("new@example.com")).thenReturn(false);

        User user = User.builder().id(UUID.randomUUID()).name("Name").email("new@example.com").role(Role.USER).build();
        when(mapper.toUser(request, Role.USER)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);

        User result = userService.createUser(request, Role.USER);
        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void updateUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        UpdateUserRequest request = new UpdateUserRequest("New Name", "new@example.com", "newPass", "photo");
        when(repository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.updateUser(userId, request)
        );
        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void getUserById_ShouldThrowUserNotFoundException_WhenNotFound() {
        UUID userId = UUID.randomUUID();
        when(repository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.getUserById(userId)
        );
        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void findAllUsers_ShouldReturnListOfUsers() {
        User user = User.builder().id(UUID.randomUUID()).name("User1").email("user1@example.com").role(Role.USER).build();
        UserResponse response = new UserResponse("User1", "user1@example.com", Role.USER.name(), null);
        when(repository.findAll()).thenReturn(Collections.singletonList(user));
        when(mapper.fromUser(user)).thenReturn(response);

        List<UserResponse> users = userService.findAllUsers();
        assertEquals(1, users.size());
        assertEquals("User1", users.getFirst().name());
    }

    @Test
    void deleteUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        when(repository.existsById(userId)).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.deleteUser(userId)
        );
        assertTrue(exception.getMessage().contains("User with id"));
    }
}
