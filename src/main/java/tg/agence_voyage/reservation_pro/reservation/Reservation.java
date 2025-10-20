package tg.agence_voyage.reservation_pro.reservation;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import tg.agence_voyage.reservation_pro.client.Client;
import tg.agence_voyage.reservation_pro.typebillet.TypeBillet;
import tg.agence_voyage.reservation_pro.voyage.Voyage;

@Entity
@Getter
@Setter
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation", nullable = false)
    private int idReservation;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_voyage", nullable = false)
    private Voyage voyage;

    @ManyToOne
    @JoinColumn(name = "id_type_billet", nullable = false)
    private TypeBillet typeBillet;

    @Column(name = "date_reservation", nullable = false)
    private LocalDateTime dateReservation;

    @Column(name = "date_depart", nullable = false)
    private LocalDateTime dateDepart;

    @Column(name = "nombre_de_reserve", nullable = false)
    private int nombreDeReserve;

    @Column(name = "montant_total", nullable = false)
    private double montantTotal;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idReservation;
        result = prime * result + ((client == null) ? 0 : client.getIdClient());
        result = prime * result + ((voyage == null) ? 0 : voyage.getIdVoyage());
        result = prime * result + ((typeBillet == null) ? 0 : typeBillet.getIdTypeBillet());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Reservation other = (Reservation) obj;
        if (idReservation != other.idReservation)
            return false;
        if (client == null) {
            if (other.client != null)
                return false;
        } else if (!client.equals(other.client))
            return false;
        if (voyage == null) {
            if (other.voyage != null)
                return false;
        } else if (!voyage.equals(other.voyage))
            return false;
        if (typeBillet == null) {
            if (other.typeBillet != null)
                return false;
        } else if (!typeBillet.equals(other.typeBillet))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Reservation [idReservation=" + idReservation + ", client=" + (client != null ? client.getIdClient() : null)
                + ", voyage=" + (voyage != null ? voyage.getIdVoyage() : null) + ", typeBillet=" + (typeBillet != null ? typeBillet.getIdTypeBillet() : null)
                + ", dateReservation=" + dateReservation + ", dateDepart=" + dateDepart + ", nombreDeReserve=" + nombreDeReserve
                + ", montantTotal=" + montantTotal + ", status=" + status + "]";
    }
}