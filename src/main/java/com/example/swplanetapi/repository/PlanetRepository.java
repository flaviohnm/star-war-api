package com.example.swplanetapi.repository;

import com.example.swplanetapi.model.Planet;
import org.springframework.data.repository.CrudRepository;

public interface PlanetRepository extends CrudRepository<Planet, Long> {
}
