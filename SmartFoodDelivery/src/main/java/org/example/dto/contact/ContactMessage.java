package org.example.dto.contact;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Data
public class ContactMessage {
    @NotEmpty(message = "İsim alanı boş bırakılamaz")
    private String name;

    @NotEmpty(message = "Email alanı boş bırakılamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    @NotEmpty(message = "Konu alanı boş bırakılamaz")
    private String subject;

    @NotEmpty(message = "Mesaj alanı boş bırakılamaz")
    private String message;
}