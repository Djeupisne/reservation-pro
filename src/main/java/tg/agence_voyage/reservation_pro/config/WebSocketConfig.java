package tg.agence_voyage.reservation_pro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Activation du broker avec un préfixe de destination pour l'envoi de messages
        registry.enableSimpleBroker("/topic", "/queue"); // Ajout de /queue pour les messages privés
        registry.setApplicationDestinationPrefixes("/app"); // Préfixe des messages envoyés au serveur
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200") // Autorisation des requêtes cross-origin
                .withSockJS(); // Support de SockJS pour compatibilité avec anciens navigateurs
    }
}