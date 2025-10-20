package tg.agence_voyage.reservation_pro.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tg.agence_voyage.reservation_pro.service.CityService;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @Operation(summary = "Récupérer toutes les villes", description = "Retourne la liste de toutes les villes.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des villes récupérée"),
        @ApiResponse(responseCode = "401", description = "Non authentifié"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @Operation(summary = "Récupérer une ville par ID", description = "Retourne une ville spécifique par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ville trouvée"),
        @ApiResponse(responseCode = "404", description = "Ville non trouvée"),
        @ApiResponse(responseCode = "401", description = "Non authentifié"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        return cityService.getCityById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}