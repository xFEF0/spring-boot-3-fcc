package com.xfef0.runnerz.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RunControllerIntegrationTest {

    public static final String BASE_URI = "/api/runs";
    @LocalServerPort
    int serverPort;

    RestClient restClient;

    @BeforeEach
    void setup() {
        restClient = RestClient.create("http://localhost:" + serverPort);
    }

    @Test
    void shouldFindAllRuns() {
        List<Run> runs = getAllRuns();

        assertNotNull(runs);
        assertTrue(runs.size() >= 8);
    }

    @Test
    void shouldFindRunById() {
        Run run = getRunById(1);

        assertNotNull(run);

        // Data from runs.json
        assertEquals(1, run.id());
        assertEquals("Monday morning run", run.title());
        assertEquals(LocalDateTime.parse("2024-11-01T10:07:05.000000"), run.startedOn());
        assertEquals(LocalDateTime.parse("2024-11-01T10:54:00.000000"), run.completedOn());
        assertEquals(5, run.kilometers());
        assertEquals(Location.OUTDOOR, run.location());
        assertEquals(0, run.version());
    }

    @Test
    void shouldFindRunsByLocation() {
        List<Run> runs = restClient.get()
                .uri(BASE_URI + "/location/INDOOR")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});


        assertNotNull(runs);
        assertEquals(3, runs.size());
        runs.forEach(run -> assertEquals(Location.INDOOR, run.location()));
    }

    @Test
    void shouldCreateARun() {
        Run run = new Run(
                33,
                "Test Create Run",
                LocalDateTime.parse("2024-12-01T10:07:05.000000"),
                LocalDateTime.parse("2024-12-01T10:30:05.000000"),
                3,
                Location.OUTDOOR,
                null
        );

        ResponseEntity<Void> response = restClient.post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .body(run)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Run createdRun = getRunById(33);
        assertNotNull(createdRun);
        assertEquals(33, createdRun.id());
    }


    @Test
    void shouldUpdateRun() {
        int id = 8;
        Run run = getRunById(id);

        assertNotNull(run);
        assertFalse(run.title().endsWith("UP"));

        Run upRun = new Run(run.id(),
                run.title()+"UP",
                run.startedOn(),
                run.completedOn(),
                3,
                run.location(),
                run.version());

        ResponseEntity<Void> response = restClient.put()
                .uri(BASE_URI + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(upRun)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        run = getRunById(id);
        assertNotNull(run);
        assertTrue(run.title().endsWith("UP"));
    }

    @Test
    void shouldDeleteRun() {
        List<Run> runs = getAllRuns();
        int sizeBeforeDelete = runs.size();

        ResponseEntity<Void> response = restClient.delete()
                .uri(BASE_URI + "/" + 7)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        runs = getAllRuns();
        assertTrue(sizeBeforeDelete > runs.size());
    }

    @Test
    void shouldFailDeleteForInvalidId() {
        try {
            ResponseEntity<Void> respo = restClient.delete()
                    .uri(BASE_URI + "/" + 22)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            (request, response) -> {
                                throw new RunNotFoundException();
                            })
                    .toBodilessEntity();
        } catch (Exception e) {
            assertInstanceOf(RunNotFoundException.class, e);
        }
    }

    @Test
    void shouldFailToFindByInvalidId() {
        try {
            getRunById(99);
        } catch (Exception e) {
            assertInstanceOf(RunNotFoundException.class, e);
        }
    }

    private Run getRunById(int id) {
        return restClient.get()
                .uri(BASE_URI + "/" + id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            throw new RunNotFoundException();
                        })
                .body(Run.class);
    }

    private List<Run> getAllRuns() {
        return restClient.get()
                .uri(BASE_URI)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
