package tg.agence_voyage.reservation_pro.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tg.agence_voyage.reservation_pro.reservation.ReservationRepository;

@RestController
@RequestMapping("/api/statistics")
@PreAuthorize("hasRole('ADMIN')") // Restreindre à ADMIN
public class StatisticsController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Operation(summary = "Compter les réservations par client", description = "Retourne le nombre de réservations pour un client spécifique.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre de réservations retourné"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping("/reservations/client/{idClient}")
    public ResponseEntity<Long> getReservationCountByClient(@PathVariable int idClient) {
        long count = reservationRepository.countByClientIdClient(idClient);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Compter les réservations par voyage", description = "Retourne le nombre de réservations pour un voyage spécifique.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre de réservations retourné"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping("/reservations/voyage/{idVoyage}")
    public ResponseEntity<Long> getReservationCountByVoyage(@PathVariable int idVoyage) {
        long count = reservationRepository.countByVoyageIdVoyage(idVoyage);
        return ResponseEntity.ok(count);
    }
}