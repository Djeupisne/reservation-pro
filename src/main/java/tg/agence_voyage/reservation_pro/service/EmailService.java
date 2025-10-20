package tg.agence_voyage.reservation_pro.service;

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

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendReservationConfirmation(String toEmail, Reservation reservation) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, new EmailMessage(toEmail, reservation, "confirmation"));
    }

    public void sendReservationCancellation(String toEmail, Reservation reservation) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, new EmailMessage(toEmail, reservation, "cancellation"));
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void receiveMessage(EmailMessage emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailMessage.getToEmail());
        Reservation reservation = emailMessage.getReservation();

        if ("confirmation".equals(emailMessage.getType())) {
            message.setSubject("Confirmation de votre réservation");
            message.setText("Bonjour,\n\n" +
                    "Votre réservation a été confirmée avec succès.\n" +
                    "Détails de la réservation :\n" +
                    "ID Réservation : " + reservation.getIdReservation() + "\n" +
                    "Voyage : " + reservation.getVoyage().getVilleDepart() + " à " + reservation.getVoyage().getCity().getName() + "\n" +
                    "Date de départ : " + reservation.getDateDepart() + "\n" +
                    "Montant total : " + reservation.getMontantTotal() + " EUR\n\n" +
                    "Merci de voyager avec nous !");
        } else if ("cancellation".equals(emailMessage.getType())) {
            message.setSubject("Annulation de votre réservation");
            message.setText("Bonjour,\n\n" +
                    "Votre réservation a été annulée.\n" +
                    "Détails de la réservation annulée :\n" +
                    "ID Réservation : " + reservation.getIdReservation() + "\n" +
                    "Voyage : " + reservation.getVoyage().getVilleDepart() + " à " + reservation.getVoyage().getCity().getName() + "\n" +
                    "Date de départ : " + reservation.getDateDepart() + "\n" +
                    "Montant total : " + reservation.getMontantTotal() + " EUR\n\n" +
                    "Nous espérons vous revoir bientôt !");
        }

        mailSender.send(message);
    }
}

// Classe utilitaire pour RabbitMQ
class EmailMessage {
    private String toEmail;
    private Reservation reservation;
    private String type;

    public EmailMessage(String toEmail, Reservation reservation, String type) {
        this.toEmail = toEmail;
        this.reservation = reservation;
        this.type = type;
    }

    public String getToEmail() {
        return toEmail;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public String getType() {
        return type;
    }
}