package com.motus.fileoperations.controller;

import com.motus.fileoperations.dto.UserDto;
import com.motus.fileoperations.exception.TokenRefreshException;
import com.motus.fileoperations.model.RefreshToken;
import com.motus.fileoperations.payload.request.LoginRequest;
import com.motus.fileoperations.payload.request.SignupRequest;
import com.motus.fileoperations.payload.request.TokenRefreshRequest;
import com.motus.fileoperations.payload.response.JwtResponse;
import com.motus.fileoperations.payload.response.MessageResponse;
import com.motus.fileoperations.payload.response.TokenRefreshResponse;
import com.motus.fileoperations.security.jwt.JWTGenerator;
import com.motus.fileoperations.security.service.RefreshTokenService;
import com.motus.fileoperations.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Authentication Processes")
@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JWTGenerator jwtGenerator;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Operation(summary = "Login with a username and password")
    @Schema(title = "Sign in operation", description = "Sign in a user")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Parameter(description = "Username and password inside a JSON object with double quotes") @Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtGenerator.generateToken(authentication);
        UserDto user = userService.findByUserName(loggedInUser.getName());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loggedInUser.getName());
        return ResponseEntity.ok(new JwtResponse(token, refreshToken.getToken(), user.getId(), user.getUsername(), user.getEmail()));
    }

    @Operation(summary = "Register with a username and password")
    @Schema(title = "Register operation", description = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Parameter(description = "Username and password inside a JSON object with double quotes") @RequestBody SignupRequest registerDto) {
        if (Boolean.TRUE.equals(userService.existsByUsername(registerDto.getUsername()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        } else if (Boolean.TRUE.equals(userService.existsByEmail(registerDto.getEmail()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        UserDto userDto = new UserDto();
        userDto.setUsername(registerDto.getUsername());
        userDto.setPassword(passwordEncoder.encode((registerDto.getPassword())));
        userDto.setEmail(registerDto.getEmail());
        userService.saveUser(userDto);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @Operation(summary = "Refresh the token")
    @Schema(title = "Return the refresh token", description = "Return the refresh token")
    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshtoken(@Parameter(description = "Refresh token inside as a string") @Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration).map(RefreshToken::getUser).map(user -> {
            String token = jwtGenerator.generateTokenFromUsername(user.getUsername());
            return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        }).orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    @Operation(summary = "Logout the user")
    @Schema(title = "Sign out operation", description = "Sign out the user")
    @PostMapping("/signout")
    public ResponseEntity<MessageResponse> logoutUser() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.findByUserName(loggedInUser.getName());
        refreshTokenService.deleteByUserId(user.getId());
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }
}
