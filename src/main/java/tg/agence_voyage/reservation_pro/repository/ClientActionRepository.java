package tg.agence_voyage.reservation_pro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.agence_voyage.reservation_pro.entity.ClientAction;

public interface ClientActionRepository extends JpaRepository<ClientAction, Long> {
}