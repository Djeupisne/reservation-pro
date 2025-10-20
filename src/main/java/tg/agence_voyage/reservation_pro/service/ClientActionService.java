package tg.agence_voyage.reservation_pro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tg.agence_voyage.reservation_pro.entity.ClientAction;
import tg.agence_voyage.reservation_pro.event.ActionSavedEvent;
import tg.agence_voyage.reservation_pro.repository.ClientActionRepository;
import tg.agence_voyage.reservation_pro.client.Client;
import tg.agence_voyage.reservation_pro.client.ClientRepository;

@Service
public class ClientActionService {
    @Autowired
    private ClientActionRepository clientActionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public ClientAction saveClientAction(ClientAction clientAction) {
        if (clientAction.getClient() != null) {
            Client client = clientRepository.findById(clientAction.getClient().getIdClient())
                    .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientAction.getClient().getIdClient()));
            clientAction.setClient(client);
        }
        ClientAction savedAction = clientActionRepository.save(clientAction);
        eventPublisher.publishEvent(new ActionSavedEvent(this));
        return savedAction;
    }

    public Iterable<ClientAction> getAllClientActions() {
        return clientActionRepository.findAll();
    }
}