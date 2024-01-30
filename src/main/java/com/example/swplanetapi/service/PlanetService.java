package com.example.swplanetapi.service;

import com.example.swplanetapi.model.Planet;
import com.example.swplanetapi.repository.PlanetRepository;
import org.springframework.stereotype.Service;

@Service
public class PlanetService {

    private PlanetRepository repository;
    public PlanetService(PlanetRepository repository) {
        this.repository = repository;
    }

    public Planet create(Planet planet) {
        return repository.save(planet);
    }
}
