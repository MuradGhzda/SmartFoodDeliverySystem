package org.example.service.user;

import org.example.dto.auth.UserRegistrationRequest;
import org.example.model.user.Address;
import org.example.model.user.User;

import java.util.List;

public interface UserService {
    User save(User user);

    User saveAddress(User user, Address address);

    User registerUser(UserRegistrationRequest request);

    User updateUser(User user);

    User getCurrentUser();

    User findByEmail(String email);

    User findByUsername(String username);

    void deleteAddress(User user, Long addressId);

    long getUserCount();

    void updateUserRole(Long userId, String role);

    void deleteUser(Long userId);

    List<User> getAllUsers();

}