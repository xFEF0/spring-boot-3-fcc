package com.xfef0.runnerz.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(RunController.class)
class RunControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    RunRepository repository;

    private final List<Run> runs = new ArrayList<>();

    @BeforeEach
    void setup() {
        runs.add(
                new Run(1,
                        "Monday morning run",
                        LocalDateTime.now().minusDays(5),
                        LocalDateTime.now().minusDays(5).plusHours(1),
                        6,
                        Location.INDOOR,
                        null)
        );
    }

    @Test
    void shouldFindAllRuns() throws Exception {
        when(repository.findAll()).thenReturn(runs);
        mvc.perform(get("/api/runs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(runs.size()));
    }

    @Test
    void shouldFindRunById() throws Exception {
        Run run = runs.get(0);
        when(repository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Optional.of(run));
        mvc.perform(get("/api/runs/{id}", run.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(run.id()))
                .andExpect(jsonPath("$.title").value(run.title()))
                .andExpect(jsonPath("$.kilometers").value(run.kilometers()))
                .andExpect(jsonPath("$.location").value(run.location().name()));

    }
}