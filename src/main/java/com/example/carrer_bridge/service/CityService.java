package com.example.carrer_bridge.service;

import com.example.carrer_bridge.domain.entities.City;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CityService {
    City save(City city);
    List<City> findAll();
    Optional<City> findById(Long id);
    City update(City cityUpdated, Long id);
    void delete(Long id);
}
