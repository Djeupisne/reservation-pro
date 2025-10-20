package tg.agence_voyage.reservation_pro.reservation;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import tg.agence_voyage.reservation_pro.client.Client;
import tg.agence_voyage.reservation_pro.client.ClientRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private static final String CLIENT_NOT_FOUND = "Client non trouvé";

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public ResponseEntity<?> getAllReservations() {
        try {
            String loginClient = SecurityContextHolder.getContext().getAuthentication().getName();
            Client client = clientRepository.findByMailClient(loginClient)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENT_NOT_FOUND));

            List<Reservation> reservations;
            if (client.getRole().equals("ROLE_ADMIN")) {
                reservations = reservationRepository.findAll();
            } else {
                reservations = reservationRepository.findByClientIdClient(client.getIdClient());
            }
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des réservations: " + e.getMessage());
        }
    }

    // Nouvel endpoint pour récupérer les réservations par client ID
    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> getReservationsByClientId(@PathVariable int clientId) {
        try {
            String loginClient = SecurityContextHolder.getContext().getAuthentication().getName();
            Client requestingClient = clientRepository.findByMailClient(loginClient)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENT_NOT_FOUND));

            // Vérification des permissions
            if (!requestingClient.getRole().equals("ROLE_ADMIN") && 
                requestingClient.getIdClient() != clientId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Accès non autorisé à ces réservations");
            }

            List<Reservation> reservations = reservationRepository.findByClientIdClient(clientId);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des réservations client: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable int id) {
        try {
            String loginClient = SecurityContextHolder.getContext().getAuthentication().getName();
            Client client = clientRepository.findByMailClient(loginClient)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENT_NOT_FOUND));

            Optional<Reservation> reservation = reservationRepository.findById(id);
            if (reservation.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Reservation res = reservation.get();
            if (!client.getRole().equals("ROLE_ADMIN") && res.getClient().getIdClient() != client.getIdClient()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération de la réservation: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody Reservation reservation) {
        try {
            String loginClient = SecurityContextHolder.getContext().getAuthentication().getName();
            Client client = clientRepository.findByMailClient(loginClient)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENT_NOT_FOUND));

            reservation.setClient(client);
            Reservation savedReservation = reservationService.calculateAndSaveReservation(reservation);
            return ResponseEntity.ok(savedReservation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création de la réservation: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveReservation(@PathVariable int id) {
        try {
            Reservation approvedReservation = reservationService.approveReservation(id);
            return ResponseEntity.ok(approvedReservation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'approbation de la réservation: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectReservation(@PathVariable int id) {
        try {
            Reservation rejectedReservation = reservationService.rejectReservation(id);
            return ResponseEntity.ok(rejectedReservation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du rejet de la réservation: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/approve-payment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approvePayment(@PathVariable int id) {
        try {
            Optional<Reservation> optionalReservation = reservationRepository.findById(id);
            if (optionalReservation.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Reservation reservation = optionalReservation.get();
            if (!reservation.getStatus().equals("APPROVED")) {
                return ResponseEntity.badRequest().body("Réservation non approuvée");
            }

            reservation.setStatus("PAID");
            return ResponseEntity.ok(reservationRepository.save(reservation));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'approbation du paiement: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> cancelReservation(@PathVariable int id) {
        try {
            String loginClient = SecurityContextHolder.getContext().getAuthentication().getName();
            Client client = clientRepository.findByMailClient(loginClient)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENT_NOT_FOUND));

            Optional<Reservation> optionalReservation = reservationRepository.findById(id);
            if (optionalReservation.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Reservation reservation = optionalReservation.get();
            if (!client.getRole().equals("ROLE_ADMIN") && reservation.getClient().getIdClient() != client.getIdClient()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            reservation.setStatus("CANCELLED");
            return ResponseEntity.ok(reservationRepository.save(reservation));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'annulation de la réservation: " + e.getMessage());
        }
    }
}