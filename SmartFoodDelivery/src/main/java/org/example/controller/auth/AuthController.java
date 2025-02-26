package org.example.controller.auth;

import org.example.dto.auth.UserRegistrationRequest;
import org.example.service.auth.impl.AuthServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    public AuthController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               RedirectAttributes redirectAttributes) {
        try {
            authServiceImpl.login(username, password);
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/login";
        }
    }
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Kullanıcı adı veya şifre hatalı!");
        }
        if (logout != null) {
            model.addAttribute("message", "Başarıyla çıkış yapıldı.");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRegistrationRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserRegistrationRequest request,
                               RedirectAttributes redirectAttributes) {
        try {
            authServiceImpl.registerUser(request);
            redirectAttributes.addFlashAttribute("success", "Kayıt başarılı! Lütfen giriş yapın.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }

    @GetMapping("/logout")  // POST yerine GET kullanıyoruz
    public String logout(RedirectAttributes redirectAttributes) {
        try {
            authServiceImpl.logout();
            redirectAttributes.addFlashAttribute("message", "Başarıyla çıkış yapıldı.");
            return "redirect:/?logout";  // Ana sayfaya logout parametresi ile yönlendir
        } catch (Exception e) {
            return "redirect:/";
        }
    }
}