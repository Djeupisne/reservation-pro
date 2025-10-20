package tg.agence_voyage.reservation_pro.voyage;

import java.sql.Timestamp;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tg.agence_voyage.reservation_pro.entity.City;
import tg.agence_voyage.reservation_pro.reservation.Reservation;

@Entity
@Getter
@Setter
@Table(name = "voyage")
public class Voyage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voyage", nullable = false)
    private int idVoyage;

    @Column(name = "ville_depart", length = 50)
    private String villeDepart;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false) // Updated to non-nullable
    private City city;

    @Column(name = "date_depart", nullable = false)
    private Timestamp dateDepart;

    @Column(name = "date_arrivee", nullable = true)
    private Timestamp dateArrivee;

    @Column(name = "moyen_transport", length = 50)
    private String moyenTransport;

    @Column(name = "prix_voyage")
    private double prixVoyage;

    @Column(name = "places_disponibles", nullable = false)
    private int placesDisponibles;

    @JsonIgnore
    @OneToMany(mappedBy = "voyage", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idVoyage;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Voyage other = (Voyage) obj;
        return idVoyage == other.idVoyage;
    }

    @Override
    public String toString() {
        return "Voyage [idVoyage=" + idVoyage + ", villeDepart=" + villeDepart + ", city=" + (city != null ? city.getName() : null)
                + ", dateDepart=" + dateDepart + ", dateArrivee=" + dateArrivee + ", moyenTransport=" + moyenTransport
                + ", prixVoyage=" + prixVoyage + ", placesDisponibles=" + placesDisponibles + "]";
    }
}