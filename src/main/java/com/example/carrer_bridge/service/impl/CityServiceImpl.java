package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.City;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.CityRepository;
import com.example.carrer_bridge.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public City save(City city) {
        Optional<City> existingCity= cityRepository.findByName(city.getName());
        if (existingCity.isPresent()){
            throw new OperationException("City already exists with type: " + city.getName());
        }
        return cityRepository.save(city);
    }

    @Override
    public List<City> findAll() {
        List<City> cities = cityRepository.findAll();
        if (cities.isEmpty()) {
            throw new OperationException("No cities found!");
        } else {
            return cities;
        }
    }

    @Override
    public Optional<City> findById(Long id) {
        Optional<City> city = cityRepository.findById(id);
        if (city.isEmpty()) {
            throw new OperationException("No city found for this ID!");
        } else {
            return city;
        }
    }

    @Override
    public City update(City cityUpdated, Long id) {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new OperationException("City not found with id: " + id));

        existingCity.setName(cityUpdated.getName());
        return cityRepository.save(existingCity);
    }

    @Override
    public void delete(Long id) {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new OperationException("City not found with id: " + id));
        cityRepository.delete(existingCity);
    }
}
