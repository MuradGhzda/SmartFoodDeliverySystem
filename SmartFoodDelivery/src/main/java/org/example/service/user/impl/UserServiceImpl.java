package org.example.service.user.impl;

import jakarta.annotation.PostConstruct;
import org.example.dto.auth.UserRegistrationRequest;
import org.example.model.user.User;
import org.example.model.user.Role;
import org.example.model.user.Address;
import org.example.repository.cart.CartRepository;
import org.example.repository.user.AddressRepository;
import org.example.repository.user.UserRepository;
import org.example.repository.user.RoleRepository;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.UserAlreadyExistsException;
import org.example.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepository addressRepository;
   // private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User saveAddress(User user, Address address) {
        address.setUser(user);
        if(address.isDefault()) {
            // Diğer adreslerin varsayılan durumunu kaldır
            user.getAddresses().stream()
                    .filter(a -> !a.equals(address))
                    .forEach(a -> a.setDefault(false));
        }
        user.addAddress(address);
        return userRepository.save(user);
    }

    public User registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setAddresses(new ArrayList<>());

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.addRole(userRole);

        return userRepository.save(user);
    }

    public User updateUser(User user) {
        User existingUser = getCurrentUser();

        if (existingUser == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (!existingUser.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        if (!existingUser.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already taken");
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPhone(user.getPhone());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public void deleteAddress(User user, Long addressId) {
        Address addressToDelete = user.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        user.removeAddress(addressToDelete);
        userRepository.save(user);
    }

    public long getUserCount() {
        return userRepository.count();
    }

    @Transactional
    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı"));

        // Clear existing roles
        user.getRoles().clear();

        // Find or create the new role
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role(roleName);
                    return roleRepository.save(newRole);
                });

        // Add the new role
        user.addRole(role);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getOrders().isEmpty()) {
            throw new IllegalStateException("Cannot delete user with existing orders");
        }

        userRepository.delete(user);
    }
    @PostConstruct
    public void initializeAdminUser() {
        // Check if admin role exists
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElse(new Role("ROLE_ADMIN"));
        roleRepository.save(adminRole);

        // Check if admin user exists
        if (!userRepository.existsByUsername("admin")) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(passwordEncoder.encode("adminpassword"));
            adminUser.addRole(adminRole);
            userRepository.save(adminUser);
        }
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}