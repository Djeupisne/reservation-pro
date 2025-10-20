package tg.agence_voyage.reservation_pro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.agence_voyage.reservation_pro.client.Client;
import tg.agence_voyage.reservation_pro.client.ClientRepository;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Autowired
    public UserDetailsServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Recherche de l'utilisateur avec mail_client : " + username);
        Client client = clientRepository.findByMailClient(username)
            .orElseThrow(() -> {
                System.out.println("Utilisateur non trouvé avec mail_client : " + username);
                return new UsernameNotFoundException("Aucun client trouvé avec l'email : " + username);
            });
        System.out.println("Utilisateur trouvé : " + client.getMailClient() + ", mot de passe haché : " + client.getMotPasseClient());
        return client; // Retourne directement l'instance de Client, qui implémente UserDetails
    }
}