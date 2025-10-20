package tg.agence_voyage.reservation_pro.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tg.agence_voyage.reservation_pro.reservation.Reservation;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByClientIdClient(int idClient);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.client.idClient = :idClient")
    long countByClientIdClient(@Param("idClient") int idClient);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.voyage.idVoyage = :idVoyage")
    long countByVoyageIdVoyage(@Param("idVoyage") int idVoyage);
}