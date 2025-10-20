package tg.agence_voyage.reservation_pro.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ActionEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleActionSavedEvent(ActionSavedEvent event) {
        // Envoyer un message via WebSocket sur /updates
        messagingTemplate.convertAndSend("/updates", 
            new ActionUpdateMessage("Nouvelle action enregistrée", event.getEventTimestamp()));
    }
}

// Classe pour structurer le message WebSocket
class ActionUpdateMessage {
    private String message;
    private String timestamp;

    public ActionUpdateMessage(String message, String timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}