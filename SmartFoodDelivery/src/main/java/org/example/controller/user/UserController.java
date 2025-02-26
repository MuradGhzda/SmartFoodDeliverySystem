package org.example.controller.user;

import org.example.model.user.User;
import org.example.service.user.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        User user = userServiceImpl.getCurrentUser();
        model.addAttribute("user", user);
        return "users/profile";
    }
    @GetMapping("/users")
    public String userManagement(Model model) {
        List<User> users = userServiceImpl.getAllUsers();
        model.addAttribute("users", users);
        return "users";  // templates/users.html
    }

    @PostMapping("/users/{id}/role")
    public String updateUserRole(@PathVariable Long id, @RequestParam String role) {
        userServiceImpl.updateUserRole(id, role);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userServiceImpl.deleteUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userServiceImpl.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "Profil başarıyla güncellendi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/users/profile";
    }
}