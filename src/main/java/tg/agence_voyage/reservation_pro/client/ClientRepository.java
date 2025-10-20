package tg.agence_voyage.reservation_pro.client;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByLoginClient(String loginClient);
    Optional<Client> findByMailClient(String mailClient);
}