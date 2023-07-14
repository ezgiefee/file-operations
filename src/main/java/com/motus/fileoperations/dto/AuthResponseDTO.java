package com.motus.fileoperations.dto;

public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer ";
    private String refreshToken;
    private Long id;
    private String username;
    private String email;

    public AuthResponseDTO(String accessToken, String refreshToken, String username, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.email = email;
    }
}

