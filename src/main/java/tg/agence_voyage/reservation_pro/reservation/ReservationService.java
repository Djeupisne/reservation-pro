package tg.agence_voyage.reservation_pro.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tg.agence_voyage.reservation_pro.client.Client;
import tg.agence_voyage.reservation_pro.client.ClientRepository;
import tg.agence_voyage.reservation_pro.typebillet.TypeBillet;
import tg.agence_voyage.reservation_pro.typebillet.TypeBilletRepository;
import tg.agence_voyage.reservation_pro.voyage.Voyage;
import tg.agence_voyage.reservation_pro.voyage.VoyageRepository;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private TypeBilletRepository typeBilletRepository;

    public Reservation calculateAndSaveReservation(Reservation reservation) {
        Client client = clientRepository.findById(reservation.getClient().getIdClient())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client avec ID " + reservation.getClient().getIdClient() + " non trouvé"));
        Voyage voyage = voyageRepository.findById(reservation.getVoyage().getIdVoyage())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage avec ID " + reservation.getVoyage().getIdVoyage() + " non trouvé"));
        TypeBillet typeBillet = typeBilletRepository.findById(reservation.getTypeBillet().getIdTypeBillet())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TypeBillet avec ID " + reservation.getTypeBillet().getIdTypeBillet() + " non trouvé"));

        reservation.setClient(client);
        reservation.setVoyage(voyage);
        reservation.setTypeBillet(typeBillet);

        int nombreDeReserve = reservation.getNombreDeReserve();
        int placesDisponibles = voyage.getPlacesDisponibles();
        if (nombreDeReserve > placesDisponibles) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Pas assez de places disponibles pour ce voyage. Demandé: " + nombreDeReserve + ", Disponible: " + placesDisponibles);
        }

        double prixBase = typeBillet.getPrixBase();
        reservation.setMontantTotal(prixBase * nombreDeReserve);
        reservation.setStatus("PENDING");

        return reservationRepository.save(reservation);
    }

    public Reservation approveReservation(int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Réservation avec ID " + reservationId + " non trouvée"));

        if (!reservation.getStatus().equals("PENDING")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Réservation déjà traitée : " + reservation.getStatus());
        }

        Voyage voyage = reservation.getVoyage();
        int nombreDeReserve = reservation.getNombreDeReserve();
        int placesDisponibles = voyage.getPlacesDisponibles();

        if (nombreDeReserve > placesDisponibles) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Pas assez de places disponibles pour ce voyage. Demandé: " + nombreDeReserve + ", Disponible: " + placesDisponibles);
        }

        voyage.setPlacesDisponibles(placesDisponibles - nombreDeReserve);
        voyageRepository.save(voyage);

        reservation.setStatus("APPROVED");
        return reservationRepository.save(reservation);
    }

    public Reservation rejectReservation(int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Réservation avec ID " + reservationId + " non trouvée"));

        if (!reservation.getStatus().equals("PENDING")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Réservation déjà traitée : " + reservation.getStatus());
        }

        reservation.setStatus("REJECTED");
        return reservationRepository.save(reservation);
    }
}