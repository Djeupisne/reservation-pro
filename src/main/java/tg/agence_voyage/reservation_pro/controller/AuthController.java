package tg.agence_voyage.reservation_pro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tg.agence_voyage.reservation_pro.client.Client;
import tg.agence_voyage.reservation_pro.client.ClientRepository;
import tg.agence_voyage.reservation_pro.config.RestExceptionHandler.BadRequestException;
import tg.agence_voyage.reservation_pro.config.RestExceptionHandler.NotFoundException;
import tg.agence_voyage.reservation_pro.config.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<Client> register(@RequestBody Client client) {
        System.out.println("Tentative d'inscription avec login_client : " + client.getLoginClient() + ", mail_client : " + client.getMailClient());
        if (clientRepository.findByMailClient(client.getMailClient()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }
        client.setMotPasseClient(passwordEncoder.encode(client.getMotPasseClient()));
        client.setRole("ROLE_USER"); // Par défaut pour les nouveaux clients
        Client savedClient = clientRepository.save(client);
        System.out.println("Utilisateur enregistré : " + savedClient.getMailClient());
        return ResponseEntity.ok(savedClient);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Tentative de connexion avec email : " + loginRequest.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Authentification réussie pour : " + loginRequest.getEmail());

            String token = jwtTokenProvider.generateToken(authentication);
            System.out.println("Token généré pour : " + loginRequest.getEmail() + ", contenu : " + token);

            return ResponseEntity.ok(new LoginResponse("Login successful", token));
        } catch (AuthenticationException e) {
            System.out.println("Échec de l'authentification pour : " + loginRequest.getEmail() + ", erreur : " + e.getMessage());
            throw new BadRequestException("Invalid email or password");
        } catch (Exception e) {
            System.out.println("Erreur inattendue lors de la connexion pour : " + loginRequest.getEmail() + ", erreur : " + e.getMessage());
            throw new BadRequestException("An error occurred during login");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Client> getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Récupération de l'utilisateur courant avec email : " + email);
        Client client = clientRepository.findByMailClient(email)
            .orElseThrow(() -> new NotFoundException("Client not found"));
        System.out.println("Utilisateur courant trouvé : " + client.getMailClient());
        return ResponseEntity.ok(client);
    }
}

class LoginRequest {
    private String email;
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class LoginResponse {
    private String message;
    private String token;

    public LoginResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}