package org.acelera.blogmaker.services;

import io.micrometer.common.util.StringUtils;
import org.acelera.blogmaker.exception.UserAlreadyExistException;
import org.acelera.blogmaker.exception.UserNotFoundException;
import org.acelera.blogmaker.model.Role;
import org.acelera.blogmaker.model.User;
import org.acelera.blogmaker.model.dto.request.CreateUserRequest;
import org.acelera.blogmaker.model.dto.request.UpdateUserRequest;
import org.acelera.blogmaker.model.dto.response.UserResponse;
import org.acelera.blogmaker.repository.UserRepository;
import org.acelera.blogmaker.services.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private static final String USER_NOT_FOUND = "User not found with id: ";

   public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
   }

    public User createUser(CreateUserRequest request, Role role) {
       var userEmailExists = repository.existsByEmail(request.email());

        if(Boolean.TRUE.equals(userEmailExists))
            throw new UserAlreadyExistException("User already exist in database");

        return repository.save(mapper.toUser(request, role));
    }

    private void setUser(User user, UpdateUserRequest request) {
        mergeUser(user, request);
        repository.save(user);
    }

    private void mergeUser(User user, UpdateUserRequest request) {
        if(StringUtils.isNotBlank(request.name())) user.setName(request.name());

        if(StringUtils.isNotBlank(request.password())) user.setPassword(request.password());

        if(StringUtils.isNotBlank(request.email())) user.setEmail(request.email());

        if(StringUtils.isNotBlank(request.photo())) user.setPhoto(request.photo());
    }

    public UserResponse updateUser(UUID userId, UpdateUserRequest request) {
        var user = repository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + userId));

        setUser(user, request);
        return mapper.fromUser(user);
    }

    public UserResponse getUserById(UUID userId) {
        return repository
                .findById(userId)
                .map(this.mapper::fromUser)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + userId));
    }

    public List<UserResponse> findAllUsers() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::fromUser)
                .toList();
    }

    public void deleteUser(UUID userId) {
        if(!repository.existsById(userId))
            throw new UserNotFoundException(String.format("User with id: %s not found", userId));

        repository.deleteById(userId);
    }
    
    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}