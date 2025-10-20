package tg.agence_voyage.reservation_pro.typebillet;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/typebillets")
public class TypeBilletController {

    @Autowired
    private TypeBilletRepository typeBilletRepository;

    @Operation(summary = "Récupérer tous les types de billets", description = "Retourne la liste de tous les types de billets.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des types de billets récupérée"),
        @ApiResponse(responseCode = "401", description = "Non authentifié"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()") // Allow any authenticated user
    public List<TypeBillet> getAllTypeBillets() {
        return typeBilletRepository.findAll();
    }

    @Operation(summary = "Récupérer un type de billet par ID", description = "Retourne un type de billet spécifique par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Type de billet trouvé"),
        @ApiResponse(responseCode = "404", description = "Type de billet non trouvé"),
        @ApiResponse(responseCode = "401", description = "Non authentifié"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Restrict to ADMIN
    public ResponseEntity<TypeBillet> getTypeBilletById(@PathVariable int id) {
        Optional<TypeBillet> typeBillet = typeBilletRepository.findById(id);
        return typeBillet.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Créer un type de billet", description = "Crée un nouveau type de billet avec les détails fournis.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Type de billet créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "401", description = "Non authentifié"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Restrict to ADMIN
    public TypeBillet createTypeBillet(@Valid @RequestBody TypeBillet typeBillet) {
        return typeBilletRepository.save(typeBillet);
    }

    @Operation(summary = "Mettre à jour un type de billet", description = "Met à jour les détails d'un type de billet existant.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Type de billet mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Type de billet non trouvé"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "401", description = "Non authentifié"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Restrict to ADMIN
    public ResponseEntity<TypeBillet> updateTypeBillet(@PathVariable int id, @Valid @RequestBody TypeBillet typeBilletDetails) {
        Optional<TypeBillet> optionalTypeBillet = typeBilletRepository.findById(id);
        if (optionalTypeBillet.isPresent()) {
            TypeBillet typeBillet = optionalTypeBillet.get();
            typeBillet.setLibelleTypeBillet(typeBilletDetails.getLibelleTypeBillet());
            typeBillet.setDescriptionTypeBillet(typeBilletDetails.getDescriptionTypeBillet());
            typeBillet.setPrixBase(typeBilletDetails.getPrixBase());
            return ResponseEntity.ok(typeBilletRepository.save(typeBillet));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Supprimer un type de billet", description = "Supprime un type de billet par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Type de billet supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Type de billet non trouvé"),
        @ApiResponse(responseCode = "401", description = "Non authentifié"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Restrict to ADMIN
    public ResponseEntity<Void> deleteTypeBillet(@PathVariable int id) {
        if (typeBilletRepository.existsById(id)) {
            typeBilletRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}