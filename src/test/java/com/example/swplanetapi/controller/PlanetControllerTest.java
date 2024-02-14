package com.example.swplanetapi.controller;

import com.example.swplanetapi.model.Planet;
import com.example.swplanetapi.service.PlanetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.swplanetapi.commom.PlanetConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PlanetService service;

    @Test
    public void createPlanet_WithValidData_ReturnsCreated() throws Exception {
        when(service.create(PLANET)).thenReturn(PLANET);

        mockMvc.perform(
                        post("/planets")
                                .content(mapper.writeValueAsString(PLANET))
                                .contentType((MediaType.APPLICATION_JSON)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void createPlanet_WithInvalidData_ReturnsBadRequest() throws Exception {
        Planet emptyPlanet = new Planet();
        Planet invalidPlanet = new Planet("", "", "");

        mockMvc.perform(
                        post("/planets")
                                .content(mapper.writeValueAsString(emptyPlanet))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(
                        post("/planets")
                                .content(mapper.writeValueAsString(invalidPlanet))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createPlanet_WithExistingName_ReturnsConflict() throws Exception {
        when(service.create(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(
                        post("/planets")
                                .content(mapper.writeValueAsString(PLANET))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() throws Exception {
        when(service.getById(1L)).thenReturn(Optional.of(PLANET));

        mockMvc.perform(
                        get("/planets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByUnExistingId_ReturnsNotFound() throws Exception {
        mockMvc.perform(
                        get("/planets/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
        when(service.getByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(
                        get("/planets/name/" + PLANET.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByUnExistingName_ReturnsNotFound() throws Exception {
        mockMvc.perform(
                        get("/planets/name/" + PLANET.getName()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listPlants_ReturnsFilteredPlanets() throws Exception {
        when(service.getPlanets(null, null)).thenReturn(PLANETS);
        when(service.getPlanets(TATOOINE.getClimate(), TATOOINE.getTerrain())).thenReturn(List.of(TATOOINE));

        mockMvc.perform(
                        get("/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc.perform(
                        get("/planets?" + String.format("climate=%s&terrain=%s", TATOOINE.getClimate(), TATOOINE.getTerrain())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(TATOOINE));
    }

    @Test
    public void listPlants_ReturnsNoPlanets() throws Exception {
        when(service.getPlanets(null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(
                        get("/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

}
