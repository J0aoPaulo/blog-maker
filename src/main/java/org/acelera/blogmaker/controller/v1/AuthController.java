package org.acelera.blogmaker.controller.v1;

import jakarta.validation.Valid;
import org.acelera.blogmaker.model.Role;
import org.acelera.blogmaker.model.User;
import org.acelera.blogmaker.model.dto.request.AuthRequest;
import org.acelera.blogmaker.model.dto.request.CreateUserRequest;
import org.acelera.blogmaker.model.dto.response.AuthResponse;
import org.acelera.blogmaker.model.dto.response.UserDto;
import org.acelera.blogmaker.security.JwtUtil;
import org.acelera.blogmaker.security.UserDetailsImpl;
import org.acelera.blogmaker.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        
        User user = userService.findByEmail(userDetails.getUsername());
        UserDto userDto = new UserDto(user);
        
        return ResponseEntity.ok(new AuthResponse(token, userDto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody CreateUserRequest request) {
        return register(request, Role.USER);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody CreateUserRequest request) {
        return register(request, Role.ADMIN);
    }

    private ResponseEntity<AuthResponse> register(CreateUserRequest request, Role role) {
        var user = userService.createUser(request, role);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String token = jwtUtil.generateToken(userDetails);
        UserDto userDto = new UserDto(user);
        return ResponseEntity.ok(new AuthResponse(token, userDto));
    }
}