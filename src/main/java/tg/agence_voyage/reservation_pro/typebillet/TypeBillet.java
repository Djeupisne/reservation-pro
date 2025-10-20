package tg.agence_voyage.reservation_pro.typebillet;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore; // Ajouter cette importation
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import tg.agence_voyage.reservation_pro.reservation.Reservation;

@Entity
@Getter
@Setter
@Table(name = "type_billet")
public class TypeBillet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_billet", nullable = false)
    private int idTypeBillet;

    @Column(name = "libelle_type_billet", nullable = false, length = 50)
    private String libelleTypeBillet;

    @Column(name = "description_type_billet", nullable = true, length = 255)
    private String descriptionTypeBillet;

    @Column(name = "prix_base", nullable = false)
    private double prixBase;

    @JsonIgnore // Ajouter cette annotation
    @OneToMany(mappedBy = "typeBillet", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idTypeBillet;
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
        TypeBillet other = (TypeBillet) obj;
        if (idTypeBillet != other.idTypeBillet)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TypeBillet [idTypeBillet=" + idTypeBillet + ", libelleTypeBillet=" + libelleTypeBillet
                + ", descriptionTypeBillet=" + descriptionTypeBillet + ", prixBase=" + prixBase + "]";
    }
}