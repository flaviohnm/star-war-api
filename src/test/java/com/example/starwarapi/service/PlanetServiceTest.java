package com.example.starwarapi.service;

import com.example.starwarapi.model.Planet;
import com.example.starwarapi.querybuilder.QueryBuilder;
import com.example.starwarapi.repository.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.starwarapi.commom.PlanetConstants.INVALID_PLANET;
import static com.example.starwarapi.commom.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

    @InjectMocks
    private PlanetService service;

    @Mock
    private PlanetRepository repository;

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        when(repository.save(PLANET)).thenReturn(PLANET);
        //System under test
        Planet sut = service.create(PLANET);
        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {
        when(repository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> service.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = service.getById(5L);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByUnExistingId_ReturnsEmpty() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Planet> sut = service.getById(5L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {
        when(repository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = service.getByName(PLANET.getName());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByUnExistingName_ReturnsEmpty() {
        final String name = "UnExisting name";

        when(repository.findByName(name)).thenReturn(Optional.empty());

        Optional<Planet> sut = service.getByName(name);

        assertThat(sut).isEmpty();
    }

    @Test
    public void listPlanets_ReturnsAllPlanets() {
        List<Planet> planets = new ArrayList<>() {{
            add(PLANET);
        }};

        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));
        when(repository.findAll(query)).thenReturn(planets);

        List<Planet> sut = service.getPlanets(PLANET.getClimate(), PLANET.getTerrain());

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.getFirst()).isEqualTo(PLANET);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() {
        when(repository.findAll(any())).thenReturn(Collections.emptyList());

        List<Planet> sut = service.getPlanets(PLANET.getClimate(), PLANET.getTerrain());

        assertThat(sut).isEmpty();
    }

    @Test
    public void removePlanet_WithExistingId_doesNotThrowAnyException(){
        assertThatCode(() -> service.removePlanet(anyLong())).doesNotThrowAnyException();
    }

    @Test
    public void removePlanet_WithExistingId_ThrowAnyException(){
        doThrow(new RuntimeException()).when(repository).deleteById(anyLong());

        assertThatThrownBy(() -> service.removePlanet(anyLong())).isInstanceOf(RuntimeException.class);
    }
}
