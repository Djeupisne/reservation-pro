package tg.agence_voyage.reservation_pro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.agence_voyage.reservation_pro.dto.ClientActionDTO;
import tg.agence_voyage.reservation_pro.entity.ClientAction;
import tg.agence_voyage.reservation_pro.client.Client;
import tg.agence_voyage.reservation_pro.service.ClientActionService;

@RestController
@RequestMapping("/test")
public class ClientActionController {

    @Autowired
    private ClientActionService clientActionService;

    @PostMapping("/client-action")
    public ResponseEntity<ClientAction> saveClientAction(@RequestBody ClientActionDTO clientActionDTO) {
        ClientAction clientAction = new ClientAction();
        clientAction.setActionType(clientActionDTO.getActionType());
        clientAction.setDescription(clientActionDTO.getDescription());
        if (clientActionDTO.getClientId() != null) {
            Client client = new Client();
            client.setIdClient(clientActionDTO.getClientId());
            clientAction.setClient(client);
        }
        ClientAction savedAction = clientActionService.saveClientAction(clientAction);
        return ResponseEntity.ok(savedAction);
    }

    @GetMapping("/client-actions")
    public ResponseEntity<Iterable<ClientAction>> getAllClientActions() {
        Iterable<ClientAction> actions = clientActionService.getAllClientActions();
        return ResponseEntity.ok(actions);
    }
}