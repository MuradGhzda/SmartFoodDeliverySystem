package org.example.controller.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @Value("${spring.profiles.active:default}") // Inject the active profile, fallback to 'default' if not set
    private String activeProfile;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        model.addAttribute("error", "Bir hata oluştu");

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("status", statusCode);

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("error", "Sayfa bulunamadı");
                model.addAttribute("message", "Aradığınız sayfa mevcut değil");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error", "Sunucu hatası");
                model.addAttribute("message", "Bir şeyler yanlış gitti. Lütfen daha sonra tekrar deneyin");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("error", "Erişim reddedildi");
                model.addAttribute("message", "Bu sayfaya erişim yetkiniz yok");
            }
        }

        // Show detailed error messages only in the development profile
        boolean showDetails = "dev".equalsIgnoreCase(activeProfile);
        model.addAttribute("showDetails", showDetails);

        return "error";
    }
}
