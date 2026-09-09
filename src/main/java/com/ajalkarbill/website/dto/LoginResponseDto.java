package com.ajalkarbill.website.dto;

public class LoginResponseDto {

    private boolean success;
    private String message;
    private String token;
    private String tokenType;
    private String role;
    private String username;

    public LoginResponseDto() {
    }

    public LoginResponseDto(boolean success, String message, String token, String tokenType, String role, String username) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.tokenType = tokenType;
        this.role = role;
        this.username = username;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
