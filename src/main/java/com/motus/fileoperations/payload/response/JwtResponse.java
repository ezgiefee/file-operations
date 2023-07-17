package com.motus.fileoperations.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type;
    private String refreshToken;
    private Long id;
    private String username;
    private String email;

    public JwtResponse(String token, String refreshToken, Long id, String username, String email) {
        this.token = token;
        this.type = "Bearer";
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
