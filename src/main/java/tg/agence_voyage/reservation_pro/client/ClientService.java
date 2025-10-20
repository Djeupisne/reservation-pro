package tg.agence_voyage.reservation_pro.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(int id) {
        return clientRepository.findById(id);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> updateClient(int id, Client clientDetails) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            client.setNomClient(clientDetails.getNomClient());
            client.setPrenomClient(clientDetails.getPrenomClient());
            client.setSexeClient(clientDetails.getSexeClient());
            client.setTelClient(clientDetails.getTelClient());
            return Optional.of(clientRepository.save(client));
        }
        return Optional.empty();
    }

    public boolean deleteClient(int id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}