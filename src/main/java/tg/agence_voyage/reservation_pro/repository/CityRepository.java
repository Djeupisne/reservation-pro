package tg.agence_voyage.reservation_pro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.agence_voyage.reservation_pro.entity.City;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByName(String name);
}