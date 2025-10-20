package tg.agence_voyage.reservation_pro.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tg.agence_voyage.reservation_pro.entity.ClientAction;
import tg.agence_voyage.reservation_pro.event.ActionSavedEvent;
import tg.agence_voyage.reservation_pro.service.ClientActionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class UpdateHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Autowired
    private ClientActionService clientActionService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        broadcastUpdate(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @EventListener
    public void onActionSavedEvent(ActionSavedEvent event) throws Exception {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                broadcastUpdate(session);
            }
        }
    }

    private void broadcastUpdate(WebSocketSession session) throws Exception {
        List<ClientAction> clientActions = new ArrayList<>();
        for (ClientAction action : clientActionService.getAllClientActions()) { // Line 52
            clientActions.add(action);
        }

        Map<String, Object> update = new HashMap<>();
        update.put("clientActions", clientActions);
        update.put("adminActions", new ArrayList<>()); // Add admin actions if applicable

        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage = mapper.writeValueAsString(update);
        session.sendMessage(new TextMessage(jsonMessage));
    }
}