
package tg.agence_voyage.reservation_pro.voyage;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tg.agence_voyage.reservation_pro.entity.City;
import tg.agence_voyage.reservation_pro.repository.CityRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/voyage")
public class VoyageController {

    @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private CityRepository cityRepository;

    @Operation(summary = "Récupérer tous les voyages", description = "Retourne la liste de tous les voyages.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Liste des voyages récupérée")})
    @GetMapping
    public List<VoyageDTO> getAllVoyages() {
        return voyageRepository.findAll().stream()
                .map(voyage -> new VoyageDTO(
                        voyage.getIdVoyage(),
                        voyage.getVilleDepart(),
                        voyage.getCity().getName(),
                        voyage.getDateDepart(),
                        voyage.getDateArrivee(),
                        voyage.getMoyenTransport(),
                        voyage.getPrixVoyage(),
                        voyage.getPlacesDisponibles(),
                        voyage.getCity().getDescription()
                ))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Récupérer un voyage par ID", description = "Retourne un voyage spécifique par son ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Voyage trouvé"),
            @ApiResponse(responseCode = "404", description = "Voyage non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VoyageDTO> getVoyageById(@PathVariable int id) {
        Optional<Voyage> voyage = voyageRepository.findById(id);
        return voyage.map(v -> ResponseEntity.ok(new VoyageDTO(
                        v.getIdVoyage(),
                        v.getVilleDepart(),
                        v.getCity().getName(),
                        v.getDateDepart(),
                        v.getDateArrivee(),
                        v.getMoyenTransport(),
                        v.getPrixVoyage(),
                        v.getPlacesDisponibles(),
                        v.getCity().getDescription()
                )))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Rechercher des voyages", description = "Recherche des voyages par ville de départ, arrivée, et dates.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Voyages trouvés")})
    @GetMapping("/search")
    public ResponseEntity<List<VoyageDTO>> searchVoyages(
            @RequestParam(required = false) String villeDepart,
            @RequestParam(required = false) String villeArrivee,
            @RequestParam(required = false) Timestamp dateDepart,
            @RequestParam(required = false) Timestamp dateArrivee) {
        List<VoyageDTO> voyages = voyageRepository.searchVoyages(villeDepart, villeArrivee, dateDepart, dateArrivee)
                .stream()
                .map(v -> new VoyageDTO(
                        v.getIdVoyage(),
                        v.getVilleDepart(),
                        v.getCity().getName(),
                        v.getDateDepart(),
                        v.getDateArrivee(),
                        v.getMoyenTransport(),
                        v.getPrixVoyage(),
                        v.getPlacesDisponibles(),
                        v.getCity().getDescription()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(voyages);
    }

    @Operation(summary = "Créer un voyage", description = "Crée un nouveau voyage avec les détails fournis.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Voyage créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public VoyageDTO createVoyage(@Valid @RequestBody VoyageDTO voyageDTO) {
        Voyage voyage = new Voyage();
        voyage.setVilleDepart(voyageDTO.getVilleDepart());
        City city = cityRepository.findByName(voyageDTO.getVilleArrivee())
                .orElseThrow(() -> new IllegalArgumentException("City not found: " + voyageDTO.getVilleArrivee()));
        voyage.setCity(city);
        voyage.setDateDepart(voyageDTO.getDateDepart());
        voyage.setDateArrivee(voyageDTO.getDateArrivee());
        voyage.setMoyenTransport(voyageDTO.getMoyenTransport());
        voyage.setPrixVoyage(voyageDTO.getPrix());
        voyage.setPlacesDisponibles(voyageDTO.getPlacesDisponibles());
        Voyage savedVoyage = voyageRepository.save(voyage);
        return new VoyageDTO(
                savedVoyage.getIdVoyage(),
                savedVoyage.getVilleDepart(),
                savedVoyage.getCity().getName(),
                savedVoyage.getDateDepart(),
                savedVoyage.getDateArrivee(),
                savedVoyage.getMoyenTransport(),
                savedVoyage.getPrixVoyage(),
                savedVoyage.getPlacesDisponibles(),
                savedVoyage.getCity().getDescription()
        );
    }

    @Operation(summary = "Mettre à jour un voyage", description = "Met à jour les détails d'un voyage existant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Voyage mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Voyage non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VoyageDTO> updateVoyage(@PathVariable int id, @Valid @RequestBody VoyageDTO voyageDTO) {
        Optional<Voyage> optionalVoyage = voyageRepository.findById(id);
        if (optionalVoyage.isPresent()) {
            Voyage voyage = optionalVoyage.get();
            voyage.setVilleDepart(voyageDTO.getVilleDepart());
            City city = cityRepository.findByName(voyageDTO.getVilleArrivee())
                    .orElseThrow(() -> new IllegalArgumentException("City not found: " + voyageDTO.getVilleArrivee()));
            voyage.setCity(city);
            voyage.setDateDepart(voyageDTO.getDateDepart());
            voyage.setDateArrivee(voyageDTO.getDateArrivee());
            voyage.setMoyenTransport(voyageDTO.getMoyenTransport());
            voyage.setPrixVoyage(voyageDTO.getPrix());
            voyage.setPlacesDisponibles(voyageDTO.getPlacesDisponibles());
            Voyage updatedVoyage = voyageRepository.save(voyage);
            return ResponseEntity.ok(new VoyageDTO(
                    updatedVoyage.getIdVoyage(),
                    updatedVoyage.getVilleDepart(),
                    updatedVoyage.getCity().getName(),
                    updatedVoyage.getDateDepart(),
                    updatedVoyage.getDateArrivee(),
                    updatedVoyage.getMoyenTransport(),
                    updatedVoyage.getPrixVoyage(),
                    updatedVoyage.getPlacesDisponibles(),
                    updatedVoyage.getCity().getDescription()
            ));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Supprimer un voyage", description = "Supprime un voyage par son ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Voyage supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Voyage non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVoyage(@PathVariable int id) {
        if (voyageRepository.existsById(id)) {
            voyageRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

class VoyageDTO {
    private int idVoyage;
    private String villeDepart;
    private String villeArrivee;
    private Timestamp dateDepart;
    private Timestamp dateArrivee;
    private String moyenTransport;
    private double prix;
    private int placesDisponibles;
    private String description;

    public VoyageDTO(int idVoyage, String villeDepart, String villeArrivee, Timestamp dateDepart,
                     Timestamp dateArrivee, String moyenTransport, double prix, int placesDisponibles, String description) {
        this.idVoyage = idVoyage;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
        this.moyenTransport = moyenTransport;
        this.prix = prix;
        this.placesDisponibles = placesDisponibles;
        this.description = description;
    }

    // Getters
    public int getIdVoyage() { return idVoyage; }
    public String getVilleDepart() { return villeDepart; }
    public String getVilleArrivee() { return villeArrivee; }
    public Timestamp getDateDepart() { return dateDepart; }
    public Timestamp getDateArrivee() { return dateArrivee; }
    public String getMoyenTransport() { return moyenTransport; }
    public double getPrix() { return prix; }
    public int getPlacesDisponibles() { return placesDisponibles; }
    public String getDescription() { return description; }

    // Setters
    public void setVilleDepart(String villeDepart) { this.villeDepart = villeDepart; }
    public void setVilleArrivee(String villeArrivee) { this.villeArrivee = villeArrivee; }
    public void setDateDepart(Timestamp dateDepart) { this.dateDepart = dateDepart; }
    public void setDateArrivee(Timestamp dateArrivee) { this.dateArrivee = dateArrivee; }
    public void setMoyenTransport(String moyenTransport) { this.moyenTransport = moyenTransport; }
    public void setPrix(double prix) { this.prix = prix; }
    public void setPlacesDisponibles(int placesDisponibles) { this.placesDisponibles = placesDisponibles; }
}
