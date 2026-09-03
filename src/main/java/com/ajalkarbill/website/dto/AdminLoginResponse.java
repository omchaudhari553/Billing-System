package com.ajalkarbill.website.dto;

public class AdminLoginResponse {
    
    private boolean success;
    private String message;
    private String token;
    private AdminDto admin;

    public AdminLoginResponse() {
    }

    public AdminLoginResponse(boolean success, String message, String token, AdminDto admin) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.admin = admin;
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

    public AdminDto getAdmin() {
        return admin;
    }

    public void setAdmin(AdminDto admin) {
        this.admin = admin;
    }
}
