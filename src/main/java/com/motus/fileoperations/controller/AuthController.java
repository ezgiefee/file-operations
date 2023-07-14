package com.motus.fileoperations.controller;

import com.motus.fileoperations.dto.AuthResponseDTO;
import com.motus.fileoperations.dto.UserDto;
import com.motus.fileoperations.dto.RegisterDto;
import com.motus.fileoperations.security.JWTGenerator;
import com.motus.fileoperations.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Authentication Processes")
@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService,
                          PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @Operation(summary = "Login with a username and password")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Parameter(description = "Username and password inside a JSON object with double quotes")
            @RequestBody UserDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }

    @Operation(summary = "Register with a username and password")
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Parameter(description = "Username and password inside a JSON object with double quotes")
            @RequestBody RegisterDto registerDto) {
        if (userService.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        UserDto userDto = new UserDto();
        userDto.setUsername(registerDto.getUsername());
        userDto.setPassword(passwordEncoder.encode((registerDto.getPassword())));

        userService.saveUser(userDto);

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }
}
