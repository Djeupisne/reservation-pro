package tg.agence_voyage.reservation_pro.entity;

import tg.agence_voyage.reservation_pro.client.Client;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_action")
public class ClientAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "client_id_client")
    private Client client;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
}