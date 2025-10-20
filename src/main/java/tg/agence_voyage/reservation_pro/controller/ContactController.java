package tg.agence_voyage.reservation_pro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@RestController
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/api/contact")
    public ResponseEntity<String> sendContactMessage(@RequestBody ContactMessage message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("oualoumidjeupisne@gmail.com");
        mailMessage.setFrom("oualoumidjeupisne@gmail.com"); // Expéditeur par défaut
        mailMessage.setSubject("Nouvelle soumission de contact de " + message.getName());
        mailMessage.setText("Nom: " + message.getName() + "\nEmail: " + message.getEmail() + "\nMessage: " + message.getMessage());

        try {
            mailSender.send(mailMessage);
            return ResponseEntity.ok("Message envoyé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Échec de l'envoi du message: " + e.getMessage());
        }
    }
}

@Data
class ContactMessage {
    private String name;
    private String email;
    private String message;
}