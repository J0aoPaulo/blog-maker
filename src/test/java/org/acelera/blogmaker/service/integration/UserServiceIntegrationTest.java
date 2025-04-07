package org.acelera.blogmaker.service.integration;

import org.acelera.blogmaker.exception.UserAlreadyExistException;
import org.acelera.blogmaker.exception.UserNotFoundException;
import org.acelera.blogmaker.model.Role;
import org.acelera.blogmaker.model.User;
import org.acelera.blogmaker.model.dto.request.CreateUserRequest;
import org.acelera.blogmaker.model.dto.request.UpdateUserRequest;
import org.acelera.blogmaker.model.dto.response.UserResponse;
import org.acelera.blogmaker.repository.UserRepository;
import org.acelera.blogmaker.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    UserServiceIntegrationTest(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Test
    void testCreateUser_Success() {
        CreateUserRequest request = new CreateUserRequest("John Doe", "john.doe@example.com", "password", null);
        User user = userService.createUser(request, Role.USER);
        assertNotNull(user);
        assertNotNull(user.getId());

        User savedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(savedUser);
        assertEquals("john.doe@example.com", savedUser.getEmail());
    }

    @Test
    void testCreateUser_Failure_DuplicateEmail() {
        CreateUserRequest request = new CreateUserRequest("Jane Doe", "jane.doe@example.com", "password", null);
        userService.createUser(request, Role.USER);

        CreateUserRequest duplicateRequest = new CreateUserRequest("Another Jane", "jane.doe@example.com", "password", null);
        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(duplicateRequest, Role.USER));
    }

    @Test
    void testUpdateUser_Success() {
        CreateUserRequest createRequest = new CreateUserRequest("Alice", "alice@example.com", "pass", null);
        User user = userService.createUser(createRequest, Role.USER);
        UUID userId = user.getId();

        UpdateUserRequest updateRequest = new UpdateUserRequest("Alice Updated", "alice.updated@example.com", "newpass", "photoUrl");
        UserResponse updatedUser = userService.updateUser(userId, updateRequest);
        assertNotNull(updatedUser);
        assertEquals("Alice Updated", updatedUser.name());
        assertEquals("alice.updated@example.com", updatedUser.email());
    }

    @Test
    void testUpdateUser_Failure_UserNotFound() {
        UUID fakeId = UUID.randomUUID();
        UpdateUserRequest updateRequest = new UpdateUserRequest("Name", "email@example.com", "pass", "photoUrl");
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(fakeId, updateRequest));
    }

    @Test
    void testGetUserById_Success() {
        CreateUserRequest request = new CreateUserRequest("Bob", "bob@example.com", "password", null);
        User user = userService.createUser(request, Role.USER);
        UUID userId = user.getId();

        UserResponse response = userService.getUserById(userId);
        assertNotNull(response);
        assertEquals("bob@example.com", response.email());
    }

    @Test
    void testGetUserById_Failure_UserNotFound() {
        UUID fakeId = UUID.randomUUID();
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(fakeId));
    }

    @Test
    void testFindAllUsers() {
        CreateUserRequest request1 = new CreateUserRequest("User1", "user1@example.com", "pass1", null);
        CreateUserRequest request2 = new CreateUserRequest("User2", "user2@example.com", "pass2", null);
        userService.createUser(request1, Role.USER);
        userService.createUser(request2, Role.USER);

        List<UserResponse> users = userService.findAllUsers();
        assertTrue(users.size() >= 2);
    }

    @Test
    void testDeleteUser_Success() {
        CreateUserRequest request = new CreateUserRequest("Charlie", "charlie@example.com", "pass", null);
        User user = userService.createUser(request, Role.USER);
        UUID userId = user.getId();

        userService.deleteUser(userId);
        assertFalse(userRepository.findById(userId).isPresent());
    }

    @Test
    void testDeleteUser_Failure_UserNotFound() {
        UUID fakeId = UUID.randomUUID();
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(fakeId));
    }
}
