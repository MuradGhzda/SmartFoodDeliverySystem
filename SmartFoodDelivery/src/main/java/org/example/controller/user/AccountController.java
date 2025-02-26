package org.example.controller.user;

import org.example.model.user.Address;
import org.example.model.user.User;
import org.example.service.user.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserServiceImpl userServiceImpl;

    public AccountController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public String getAccount(Model model) {
        try {
            // Mevcut giriş yapmış kullanıcıyı al
            User user = userServiceImpl.getCurrentUser();
            // Model'e ekle
            model.addAttribute("user", user);
            return "account";
        } catch (Exception e) {
            // Hata durumunda login sayfasına yönlendir
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/address/add")
    public String addAddress(@ModelAttribute Address address, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = userServiceImpl.getCurrentUser();
            address.setUser(currentUser);
            currentUser.addAddress(address);
            userServiceImpl.save(currentUser);

            redirectAttributes.addFlashAttribute("success", "Adres başarıyla eklendi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Adres eklenirken bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/account";
    }

    @GetMapping("/address/delete/{id}")
    public String deleteAddress(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = userServiceImpl.getCurrentUser();
            Address addressToDelete = currentUser.getAddresses().stream()
                    .filter(address -> address.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (addressToDelete != null) {
                currentUser.removeAddress(addressToDelete);
                userServiceImpl.save(currentUser);
                redirectAttributes.addFlashAttribute("success", "Adres başarıyla silindi.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Adres bulunamadı.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Adres silinirken bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/account";
    }

    @GetMapping("/address/edit/{id}")
    public String editAddress(@PathVariable Long id, Model model) {
        try {
            User currentUser = userServiceImpl.getCurrentUser();
            Address address = currentUser.getAddresses().stream()
                    .filter(a -> a.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (address == null) {
                return "redirect:/account";
            }

            model.addAttribute("address", address);
            return "address-edit";  // address-edit.html oluşturmanız gerekecek
        } catch (Exception e) {
            return "redirect:/account";
        }
    }

    @PostMapping("/address/edit/{id}")
    public String updateAddress(@PathVariable Long id,
                                @ModelAttribute Address updatedAddress,
                                RedirectAttributes redirectAttributes) {
        try {
            User currentUser = userServiceImpl.getCurrentUser();
            Address existingAddress = currentUser.getAddresses().stream()
                    .filter(a -> a.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (existingAddress != null) {
                // Adresi güncelle
                existingAddress.setTitle(updatedAddress.getTitle());
                existingAddress.setAddressLine1(updatedAddress.getAddressLine1());
                existingAddress.setAddressLine2(updatedAddress.getAddressLine2());
                existingAddress.setCity(updatedAddress.getCity());
                existingAddress.setState(updatedAddress.getState());
                existingAddress.setZipCode(updatedAddress.getZipCode());
                existingAddress.setDefault(updatedAddress.isDefault());

                userServiceImpl.save(currentUser);
                redirectAttributes.addFlashAttribute("success", "Adres başarıyla güncellendi.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Güncellenecek adres bulunamadı.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Adres güncellenirken bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/account";
    }
}