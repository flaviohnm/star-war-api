package com.example.starwarapi.integrationtests;

import com.example.starwarapi.model.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.example.starwarapi.commom.PlanetConstants.PLANET;
import static com.example.starwarapi.commom.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/remove_planets.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/import_planets.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class PlanetsIT extends AbstractIntegrationTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void createPlanet_ReturnsCreated() {
        Planet sut = client.post().uri("/planets").bodyValue(PLANET)
                .exchange().expectStatus().isCreated().expectBody(Planet.class)
                .returnResult().getResponseBody();

        assertThat(sut.getId()).isNotNull();
        assertThat(sut.getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    public void getPlanet_ReturnsPlanet() {
        Planet sut = client.get().uri("/planets/1")
                .exchange().expectStatus().isOk().expectBody(Planet.class)
                .returnResult().getResponseBody();

        assertThat(sut.getId()).isNotNull();
        assertThat(sut.getId()).isEqualTo(TATOOINE.getId());
        assertThat(sut.getName()).isEqualTo(TATOOINE.getName());
        assertThat(sut.getClimate()).isEqualTo(TATOOINE.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(TATOOINE.getTerrain());
    }

    @Test
    public void getPlanetByName_ReturnsPlanet() {
        Planet sut = client.get().uri("/planets/name/" + TATOOINE.getName())
                .exchange().expectStatus().isOk().expectBody(Planet.class)
                .returnResult().getResponseBody();

        assertThat(sut.getName()).isEqualTo(TATOOINE.getName());
        assertThat(sut.getClimate()).isEqualTo(TATOOINE.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(TATOOINE.getTerrain());
    }

    @Test
    public void listPlanets_ReturnsAllPlanets() {
        Planet[] sut = client.get().uri("/planets")
                .exchange().expectStatus().isOk().expectBody(Planet[].class)
                .returnResult().getResponseBody();

        assertThat(sut).hasSize(3);
        assertThat(sut[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ByClimate_ReturnsPlanets() {
        Planet[] sut = client.get().uri("/planets?climate=" + TATOOINE.getClimate())
                .exchange().expectStatus().isOk().expectBody(Planet[].class)
                .returnResult().getResponseBody();

        assertThat(sut).hasSize(1);
        assertThat(sut[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ByTerrain_ReturnsPlanets() {

        Planet[] sut = client.get().uri("/planets?terrain=" + TATOOINE.getTerrain())
                .exchange().expectStatus().isOk().expectBody(Planet[].class)
                .returnResult().getResponseBody();

        assertThat(sut).hasSize(1);
        assertThat(sut[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void removePlanet_ReturnsNoContent() {

        Void sut = client.delete().uri("/planets/" + +TATOOINE.getId())
                .exchange().expectStatus().isNoContent().expectBody(Void.class)
                .returnResult().getResponseBody();
    }
}
