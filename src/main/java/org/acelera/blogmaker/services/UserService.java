package org.acelera.blogmaker.services;

import io.micrometer.common.util.StringUtils;
import org.acelera.blogmaker.exception.UserAlreadyExist;
import org.acelera.blogmaker.exception.UserNotFound;
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
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

   public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
   }

    public UUID createUser(CreateUserRequest request, Role role) {
        if(repository.existsByEmail(request.email()))
            throw new UserAlreadyExist("User already exist in database");

        var user = repository.save(mapper.toUser(request, role));
        return user.getId();
    }

    private void setUser(User user, UpdateUserRequest request) {
        mergeCustomer(user, request);
        repository.save(user);
    }

    private void mergeCustomer(User user, UpdateUserRequest request) {
        if(StringUtils.isNotBlank(request.name())) user.setName(request.name());

        if(StringUtils.isNotBlank(request.password())) user.setPassword(request.password());

        if(StringUtils.isNotBlank(request.email())) user.setEmail(request.email());

        if(StringUtils.isNotBlank(request.photo())) user.setPhoto(request.photo());
    }

    public User updateUser(UUID userId, UpdateUserRequest request) {
        var user = repository
                .findById(userId)
                .orElseThrow(() -> new UserNotFound(String.format("User with id: %s not found", userId)));;

        setUser(user, request);
        return user;
    }

    public UserResponse getUserById(UUID userId) {
        return repository
                .findById(userId)
                .map(this.mapper::fromUser)
                .orElseThrow(() -> new UserNotFound(String.format("User with id: %s not found", userId)));
    }

    public List<UserResponse> findAllUsers() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::fromUser)
                .collect(Collectors.toList());
    }

    public void deleteUser(UUID userId) {
        if(!repository.existsById(userId))
            throw new UserNotFound(String.format("User with id: %s not found", userId));

        repository.deleteById(userId);
    }
}