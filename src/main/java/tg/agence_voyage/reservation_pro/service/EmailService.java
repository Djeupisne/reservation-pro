package tg.agence_voyage.reservation_pro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.agence_voyage.reservation_pro.config.RabbitMQConfig;
import tg.agence_voyage.reservation_pro.reservation.Reservation;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    public void sendReservationConfirmation(String toEmail, Reservation reservation) {
        if (rabbitTemplate != null) {
            try {
                rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, 
                    new EmailMessage(toEmail, reservation, "confirmation"));
                logger.info("Message envoyé à RabbitMQ pour confirmation: {}", toEmail);
            } catch (Exception e) {
                logger.error("Erreur RabbitMQ, envoi direct: {}", e.getMessage());
                sendDirectEmail(toEmail, reservation, "confirmation");
            }
        } else {
            sendDirectEmail(toEmail, reservation, "confirmation");
        }
    }

    public void sendReservationCancellation(String toEmail, Reservation reservation) {
        if (rabbitTemplate != null) {
            try {
                rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, 
                    new EmailMessage(toEmail, reservation, "cancellation"));
                logger.info("Message envoyé à RabbitMQ pour annulation: {}", toEmail);
            } catch (Exception e) {
                logger.error("Erreur RabbitMQ, envoi direct: {}", e.getMessage());
                sendDirectEmail(toEmail, reservation, "cancellation");
            }
        } else {
            sendDirectEmail(toEmail, reservation, "cancellation");
        }
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void receiveMessage(EmailMessage emailMessage) {
        logger.info("Message reçu de RabbitMQ pour: {}", emailMessage.getToEmail());
        sendDirectEmail(emailMessage.getToEmail(), emailMessage.getReservation(), emailMessage.getType());
    }

    private void sendDirectEmail(String toEmail, Reservation reservation, String type) {
        // Implémentation d'email simple (logs pour le moment)
        if ("confirmation".equals(type)) {
            logger.info("CONFIRMATION EMAIL à: {} - Réservation: {}", 
                toEmail, reservation.getIdReservation());
        } else if ("cancellation".equals(type)) {
            logger.info("ANNULATION EMAIL à: {} - Réservation: {}", 
                toEmail, reservation.getIdReservation());
        }
        
        // TODO: Ajouter l'envoi d'email réel ici plus tard
        // Utiliser JavaMailSender ou un service d'email tiers
    }
}

// Classe utilitaire pour RabbitMQ
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