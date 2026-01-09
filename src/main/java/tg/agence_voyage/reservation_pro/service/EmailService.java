package tg.agence_voyage.reservation_pro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tg.agence_voyage.reservation_pro.config.RabbitMQConfig;
import tg.agence_voyage.reservation_pro.reservation.Reservation;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    public void sendReservationConfirmation(String toEmail, Reservation reservation) {
        try {
            if (rabbitTemplate != null) {
                // Utiliser RabbitMQ si disponible
                rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, 
                    new EmailMessage(toEmail, reservation, "confirmation"));
                logger.info("Message envoyé à RabbitMQ pour confirmation: {}", toEmail);
            } else {
                // Envoyer directement l'email
                sendDirectEmail(toEmail, reservation, "confirmation");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de confirmation à: {}", toEmail, e);
            // Fallback: envoyer directement
            sendDirectEmail(toEmail, reservation, "confirmation");
        }
    }

    public void sendReservationCancellation(String toEmail, Reservation reservation) {
        try {
            if (rabbitTemplate != null) {
                // Utiliser RabbitMQ si disponible
                rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, 
                    new EmailMessage(toEmail, reservation, "cancellation"));
                logger.info("Message envoyé à RabbitMQ pour annulation: {}", toEmail);
            } else {
                // Envoyer directement l'email
                sendDirectEmail(toEmail, reservation, "cancellation");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi d'annulation à: {}", toEmail, e);
            // Fallback: envoyer directement
            sendDirectEmail(toEmail, reservation, "cancellation");
        }
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void receiveMessage(EmailMessage emailMessage) {
        logger.info("Message reçu de RabbitMQ pour: {}", emailMessage.getToEmail());
        sendEmail(emailMessage);
    }

    private void sendDirectEmail(String toEmail, Reservation reservation, String type) {
        EmailMessage emailMessage = new EmailMessage(toEmail, reservation, type);
        sendEmail(emailMessage);
    }

    private void sendEmail(EmailMessage emailMessage) {
        if (mailSender == null) {
            logger.warn("JavaMailSender non configuré. Email non envoyé à: {}", 
                emailMessage.getToEmail());
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailMessage.getToEmail());
            message.setFrom("noreply@agencevoyage.com");
            Reservation reservation = emailMessage.getReservation();

            if ("confirmation".equals(emailMessage.getType())) {
                message.setSubject("Confirmation de votre réservation");
                message.setText("Bonjour,\n\n" +
                        "Votre réservation a été confirmée avec succès.\n" +
                        "Détails de la réservation :\n" +
                        "ID Réservation : " + reservation.getIdReservation() + "\n" +
                        "Voyage : " + reservation.getVoyage().getVilleDepart() + " à " + 
                        reservation.getVoyage().getCity().getName() + "\n" +
                        "Date de départ : " + reservation.getDateDepart() + "\n" +
                        "Montant total : " + reservation.getMontantTotal() + " EUR\n\n" +
                        "Merci de voyager avec nous !");
            } else if ("cancellation".equals(emailMessage.getType())) {
                message.setSubject("Annulation de votre réservation");
                message.setText("Bonjour,\n\n" +
                        "Votre réservation a été annulée.\n" +
                        "Détails de la réservation annulée :\n" +
                        "ID Réservation : " + reservation.getIdReservation() + "\n" +
                        "Voyage : " + reservation.getVoyage().getVilleDepart() + " à " + 
                        reservation.getVoyage().getCity().getName() + "\n" +
                        "Date de départ : " + reservation.getDateDepart() + "\n" +
                        "Montant total : " + reservation.getMontantTotal() + " EUR\n\n" +
                        "Nous espérons vous revoir bientôt !");
            }

            mailSender.send(message);
            logger.info("Email envoyé avec succès à: {}", emailMessage.getToEmail());
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email à: {}", 
                emailMessage.getToEmail(), e);
        }
    }
}

// Classe utilitaire pour RabbitMQ - AJOUTEZ LES GETTERS/SETTERS
class EmailMessage {
    private String toEmail;
    private Reservation reservation;
    private String type;

    // Constructeur par défaut REQUIS pour RabbitMQ
    public EmailMessage() {
    }

    public EmailMessage(String toEmail, Reservation reservation, String type) {
        this.toEmail = toEmail;
        this.reservation = reservation;
        this.type = type;
    }

    // Getters et setters REQUIS
    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}