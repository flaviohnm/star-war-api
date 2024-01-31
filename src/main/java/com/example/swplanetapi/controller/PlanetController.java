package com.example.swplanetapi.controller;

import com.example.swplanetapi.model.Planet;
import com.example.swplanetapi.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planets")
public class PlanetController {

    @Autowired
    private PlanetService service;

    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody Planet planet) {
        Planet planetCreated = service.create(planet);
        return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> getById(@PathVariable("id") Long id) {
        return service.getById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Planet> getByName(@PathVariable("name") String name) {
        return service.getByName(name).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Planet>> getPlanets(@RequestParam(required = false) String climate,
                                                   @RequestParam(required = false) String terrain) {
        List<Planet> planets = service.getPlanets(climate, terrain);
        return ResponseEntity.ok(planets);
    }

    @DeleteMapping
    public ResponseEntity<Void> removePlanet(@PathVariable("id") Long id){
        service.removePlanet(id);
        return ResponseEntity.noContent().build();
    }

}
