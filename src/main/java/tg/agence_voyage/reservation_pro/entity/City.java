package tg.agence_voyage.reservation_pro.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "BYTEA")
    private byte[] image;

    @Column(name = "prix_voyage", nullable = false)
    private double prixVoyage; // Added field
}