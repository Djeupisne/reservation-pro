package tg.agence_voyage.reservation_pro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.agence_voyage.reservation_pro.entity.City;
import tg.agence_voyage.reservation_pro.repository.CityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public Optional<City> getCityById(Long id) {
        return cityRepository.findById(id);
    }

    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}