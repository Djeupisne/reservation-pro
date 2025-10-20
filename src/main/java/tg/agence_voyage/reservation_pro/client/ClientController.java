package tg.agence_voyage.reservation_pro.client;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@PreAuthorize("hasRole('ADMIN')") // Restreindre à ADMIN
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Récupérer tous les clients", description = "Retourne la liste de tous les clients.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des clients récupérée avec succès"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Operation(summary = "Récupérer un client par ID", description = "Retourne un client spécifique par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client trouvé"),
        @ApiResponse(responseCode = "404", description = "Client non trouvé"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable int id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Créer un client", description = "Crée un nouveau client avec les détails fournis.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PostMapping
    public Client createClient(@Valid @RequestBody Client client) {
        client.setMotPasseClient(passwordEncoder.encode(client.getMotPasseClient()));
        client.setRole("ROLE_USER"); // Par défaut, ROLE_USER pour les nouveaux clients
        return clientRepository.save(client);
    }

    @Operation(summary = "Mettre à jour un client", description = "Met à jour les détails d'un client existant.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Client non trouvé"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable int id, @Valid @RequestBody Client clientDetails) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            client.setNomClient(clientDetails.getNomClient());
            client.setPrenomClient(clientDetails.getPrenomClient());
            client.setSexeClient(clientDetails.getSexeClient());
            client.setTelClient(clientDetails.getTelClient());
            client.setNationaliteClient(clientDetails.getNationaliteClient());
            client.setDateNaissClient(clientDetails.getDateNaissClient());
            client.setMailClient(clientDetails.getMailClient());
            client.setLoginClient(clientDetails.getLoginClient());
            client.setMotPasseClient(passwordEncoder.encode(clientDetails.getMotPasseClient()));
            client.setRole(clientDetails.getRole());
            return ResponseEntity.ok(clientRepository.save(client));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Supprimer un client", description = "Supprime un client par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Client non trouvé"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable int id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}