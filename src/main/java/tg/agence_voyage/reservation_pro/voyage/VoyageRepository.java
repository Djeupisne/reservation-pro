package tg.agence_voyage.reservation_pro.voyage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface VoyageRepository extends JpaRepository<Voyage, Integer> {

    @Query("SELECT v FROM Voyage v WHERE (:villeDepart IS NULL OR v.villeDepart = :villeDepart) " +
           "AND (:villeArrivee IS NULL OR v.city.name = :villeArrivee) " +
           "AND (:dateDepart IS NULL OR v.dateDepart >= :dateDepart) " +
           "AND (:dateArrivee IS NULL OR v.dateArrivee <= :dateArrivee)")
    List<Voyage> searchVoyages(
            @Param("villeDepart") String villeDepart,
            @Param("villeArrivee") String villeArrivee,
            @Param("dateDepart") Timestamp dateDepart,
            @Param("dateArrivee") Timestamp dateArrivee
    );
}