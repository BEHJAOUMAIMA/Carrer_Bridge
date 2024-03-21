package com.example.carrer_bridge.web.rest;


import com.example.carrer_bridge.domain.entities.City;
import com.example.carrer_bridge.dto.request.CityRequestDto;
import com.example.carrer_bridge.dto.response.CityResponseDto;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.CityMapper;
import com.example.carrer_bridge.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cities")
public class CityRest {

    private final CityService cityService;
    private final CityMapper cityMapper;

    @GetMapping
    public ResponseEntity<List<CityResponseDto>> getAllCities() {
        List<City> cities = cityService.findAll();
        List<CityResponseDto> cityResponseDtos = cities.stream().map(cityMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(cityResponseDtos);
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<?> getCityById(@PathVariable Long cityId) {
        City city = cityService.findById(cityId)
                .orElseThrow(() -> new OperationException("City not found with ID: " + cityId));
        CityResponseDto cityResponseDto = cityMapper.toResponseDto(city);
        return ResponseEntity.ok(cityResponseDto);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage> addCity(@Valid @RequestBody CityRequestDto cityRequestDto) {
        City city = cityService.save(cityMapper.fromRequestDto(cityRequestDto));
        if (city == null) {
            return ResponseMessage.badRequest("City not created");
        } else {
            return ResponseMessage.created("City created successfully", city);
        }
    }

    @PutMapping("/update/{cityId}")
    public ResponseEntity<ResponseMessage> updateCity(@PathVariable Long cityId, @Valid @RequestBody CityRequestDto cityRequestDto) {
        City updatedCity = cityMapper.fromRequestDto(cityRequestDto);
        City city = cityService.update(updatedCity, cityId);
        return ResponseEntity.ok(ResponseMessage.created("City updated successfully", city).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteCity(@PathVariable Long id) {
        Optional<City> existingCity = cityService.findById(id);
        if (existingCity.isEmpty()) {
            return ResponseMessage.notFound("City not found with ID: " + id);
        }
        cityService.delete(id);
        return ResponseMessage.ok("City deleted successfully with ID: " + id, null);
    }
}
