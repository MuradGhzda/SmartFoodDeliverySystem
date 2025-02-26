package org.example.controller.main;

import jakarta.validation.Valid;
import org.example.dto.contact.ContactMessage;
import org.example.service.email.impl.EmailServiceImpl;
import org.example.service.restaurant.impl.RestaurantServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {
    private final EmailServiceImpl emailServiceImpl;
    private final RestaurantServiceImpl restaurantServiceImpl;

    public MainController(EmailServiceImpl emailServiceImpl, RestaurantServiceImpl restaurantServiceImpl) {
        this.emailServiceImpl = emailServiceImpl;
        this.restaurantServiceImpl = restaurantServiceImpl;
    }

        @GetMapping("/")
        public String home() {
            return "index";
        }

    @GetMapping("/about")
    public String about() {
        return "about";
    }


    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }
    @GetMapping("/contact")
    public String contact(Model model) {
        if (!model.containsAttribute("messageForm")) {
            model.addAttribute("messageForm", new ContactMessage());
        }
        return "contact";
    }
    @PostMapping("/contact/send")
    public String sendMessage(@Valid @ModelAttribute("messageForm") ContactMessage message,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("messageForm", message);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.messageForm", result);
            return "redirect:/contact";
        }

        try {
            emailServiceImpl.sendContactFormEmail(message);
            redirectAttributes.addFlashAttribute("success",
                    "Mesajınız başarıyla gönderildi! En kısa sürede size dönüş yapacağız.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Mesaj gönderilirken bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.");
        }

        return "redirect:/contact";
    }

    @GetMapping("/css/**")
    public String css() {
        return "forward:/css/style.css";
    }

//    @GetMapping("/")
//    public String home(Model model) {
//        List<Restaurant> popularRestaurants = restaurantServiceImpl.getPopularRestaurants();
//        model.addAttribute("popularRestaurants", popularRestaurants);
//        return "index";
//    }


}