package com.example.carrer_bridge.test;

import com.example.carrer_bridge.domain.entities.City;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.CityRepository;
import com.example.carrer_bridge.service.impl.CityServiceImpl;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CityServiceImplTest {

    @Test
    public void test_save_new_city_successfully() {
        CityRepository cityRepository = mock(CityRepository.class);
        CityServiceImpl cityService = new CityServiceImpl(cityRepository);
    
        City city = new City(1L, "Dakhla");
        when(cityRepository.findByName(city.getName())).thenReturn(Optional.empty());
        when(cityRepository.save(city)).thenReturn(city);
    
        City savedCity = cityService.save(city);
    
        assertEquals(city, savedCity);
        verify(cityRepository, times(1)).findByName(city.getName());
        verify(cityRepository, times(1)).save(city);
    }

    @Test
    public void test_retrieve_all_cities_successfully() {
        CityRepository cityRepository = mock(CityRepository.class);
        CityServiceImpl cityService = new CityServiceImpl(cityRepository);
    
        List<City> cities = Arrays.asList(new City(1L, "Casablanca"), new City(2L, "Rabat"));
        when(cityRepository.findAll()).thenReturn(cities);
    
        List<City> retrievedCities = cityService.findAll();
    
        assertEquals(cities, retrievedCities);
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    public void test_retrieve_city_by_id_successfully() {
        CityRepository cityRepository = mock(CityRepository.class);
        CityServiceImpl cityService = new CityServiceImpl(cityRepository);
    
        Long cityId = 1L;
        City city = new City(cityId, "Casablanca");
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));
    
        Optional<City> retrievedCity = cityService.findById(cityId);
    
        assertTrue(retrievedCity.isPresent());
        assertEquals(city, retrievedCity.get());
        verify(cityRepository, times(1)).findById(cityId);
    }

    @Test
    public void test_update_existing_city_successfully() {
        CityRepository cityRepository = mock(CityRepository.class);
        CityServiceImpl cityService = new CityServiceImpl(cityRepository);
    
        Long cityId = 1L;
        City existingCity = new City(cityId, "Casablanca");
        City updatedCity = new City(cityId, "Salé");
    
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(existingCity));
        when(cityRepository.existsByName(updatedCity.getName())).thenReturn(false);
        when(cityRepository.save(existingCity)).thenReturn(updatedCity);
    
        City savedCity = cityService.update(updatedCity, cityId);
    
        assertEquals(updatedCity, savedCity);
        verify(cityRepository, times(1)).findById(cityId);
        verify(cityRepository, times(1)).existsByName(updatedCity.getName());
        verify(cityRepository, times(1)).save(existingCity);
    }

    @Test
    public void test_delete_existing_city_successfully() {
        CityRepository cityRepository = mock(CityRepository.class);
        CityServiceImpl cityService = new CityServiceImpl(cityRepository);
    
        Long cityId = 1L;
        City existingCity = new City(cityId, "Ouajda");
    
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(existingCity));
    
        cityService.delete(cityId);
    
        verify(cityRepository, times(1)).findById(cityId);
        verify(cityRepository, times(1)).delete(existingCity);
    }

    @Test
    public void test_save_existing_city_should_throw_operation_exception() {
        CityRepository cityRepository = mock(CityRepository.class);
        CityServiceImpl cityService = new CityServiceImpl(cityRepository);
    
        City city = new City(1L, "Casablanca");
        when(cityRepository.findByName(city.getName())).thenReturn(Optional.of(city));
    
        assertThrows(OperationException.class, () -> cityService.save(city));
        verify(cityRepository, times(1)).findByName(city.getName());
        verify(cityRepository, never()).save(city);
    }

    @Test
    public void test_retrieve_all_cities_when_no_cities_should_throw_operation_exception() {
        CityRepository cityRepository = mock(CityRepository.class);
        CityServiceImpl cityService = new CityServiceImpl(cityRepository);
    
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());
    
        assertThrows(OperationException.class, cityService::findAll);
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    public void test_retrieve_city_by_nonexistent_id_should_throw_operation_exception() {
        CityRepository cityRepository = mock(CityRepository.class);
        CityServiceImpl cityService = new CityServiceImpl(cityRepository);
    
        Long cityId = 1L;
        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());
    
        assertThrows(OperationException.class, () -> cityService.findById(cityId));
        verify(cityRepository, times(1)).findById(cityId);
    }

    @Test
    public void test_update_city_with_existing_name_should_throw_operation_exception() {
        CityRepository cityRepository = mock(CityRepository.class);
        CityServiceImpl cityService = new CityServiceImpl(cityRepository);
    
        Long cityId = 1L;
        City existingCity = new City(cityId, "Casablanca");
        City updatedCity = new City(cityId, "Rabat");
    
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(existingCity));
        when(cityRepository.existsByName(updatedCity.getName())).thenReturn(true);
    
        assertThrows(OperationException.class, () -> cityService.update(updatedCity, cityId));
        verify(cityRepository, times(1)).findById(cityId);
        verify(cityRepository, times(1)).existsByName(updatedCity.getName());
        verify(cityRepository, never()).save(existingCity);
    }

    @Test
    public void test_delete_nonexistent_city_should_throw_operation_exception() {
        CityRepository cityRepository = mock(CityRepository.class);
        CityServiceImpl cityService = new CityServiceImpl(cityRepository);
    
        Long cityId = 1L;
        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());
    
        assertThrows(OperationException.class, () -> cityService.delete(cityId));
        verify(cityRepository, times(1)).findById(cityId);
        verify(cityRepository, never()).delete(any());
    }

    @Test
    public void test_save_city_with_null_name_should_throw_operation_exception() {
        CityRepository cityRepository = mock(CityRepository.class);
        CityServiceImpl cityService = new CityServiceImpl(cityRepository);
    
        City city = new City(1L, null);
    
        assertThrows(OperationException.class, () -> cityService.save(city));
        verify(cityRepository, never()).findByName(any());
        verify(cityRepository, never()).save(any());
    }

}