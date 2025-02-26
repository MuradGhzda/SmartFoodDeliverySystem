package org.example.service.auth;

import org.example.dto.auth.UserRegistrationRequest;
import org.example.model.user.User;

public interface AuthService {
    void login(String username, String password);
    User registerUser(UserRegistrationRequest request);
    void logout();
    User getCurrentUser();
}