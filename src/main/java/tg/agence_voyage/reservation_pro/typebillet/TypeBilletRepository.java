package tg.agence_voyage.reservation_pro.typebillet;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.agence_voyage.reservation_pro.typebillet.TypeBillet;

public interface TypeBilletRepository extends JpaRepository<TypeBillet, Integer> {
}