package com.xfef0.runnerz.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRunRepositoryTest {

    InMemoryRunRepository repository;

    @BeforeEach
    void setup() {
        repository = new InMemoryRunRepository();
        repository.create(new Run(1,
                "First run",
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(5).plusHours(1),
                6,
                Location.INDOOR,
                null));
        repository.create(new Run(2,
                "Wednesday run",
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(3).plusMinutes(40),
                5,
                Location.OUTDOOR,
                null));
    }

    @Test
    void shouldFindAllRuns() {
        List<Run> runs = repository.findAll();
        assertEquals(2, runs.size(), "Should have returned 2 runs");
    }

    @Test
    void shouldFindRunForValidId() {
        Optional<Run> run = repository.findById(1);
        assertTrue(run.isPresent());
        assertEquals("First run", run.get().title());
    }

    @Test
    void shouldNotFindRunForInvalidId() {
        RunNotFoundException exception = assertThrows(
                RunNotFoundException.class,
                () -> repository.findById(99)
        );
        assertEquals("Run Not Found", exception.getMessage());
    }

    @Test
    void shouldUpdate() {
        Run run = new Run(3,
                "Third run",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1).plusMinutes(45),
                6,
                Location.INDOOR,
                null);
        repository.create(run);
        Run runUpdated = new Run(3,
                "Updated Run",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1).plusMinutes(45),
                6,
                Location.INDOOR,
                null);
        repository.update(runUpdated, runUpdated.id());
        Optional<Run> run3 = repository.findById(runUpdated.id());
        assertTrue(run3.isPresent());
        Run run3Found = run3.get();
        assertEquals(runUpdated.title(), run3Found.title());
    }

    @Test
    void shouldNotUpdate() {
        Run run = new Run(3,
                "Third run",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1).plusMinutes(45),
                6,
                Location.INDOOR,
                null);
        RunNotFoundException exception = assertThrows(
                RunNotFoundException.class,
                () -> repository.update(run, run.id())
        );
        assertEquals("Run Not Found", exception.getMessage());
    }

    @Test
    void shouldDelete() {
        assertEquals(2, repository.count());
        repository.delete(1);
        assertEquals(1, repository.count());
        assertEquals(2, repository.findAll().get(0).id());
    }

    @Test
    void shouldNotDelete() {
        assertEquals(2, repository.count());
        repository.delete(5);
        assertEquals(2, repository.count());
    }

    @Test
    void shouldCountTwo() {
        assertEquals(2, repository.count());
    }

    @Test
    void shouldSaveAll() {
        List<Run> runs = new ArrayList<>();
        runs.add(new Run(3,
                "Run 3",
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(2).plusMinutes(30),
                2,
                Location.OUTDOOR,
                null)
        );
        runs.add(new Run(4,
                "4th run",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1).plusHours(2),
                8,
                Location.OUTDOOR,
                null)
        );
        assertEquals(2, repository.count());
        repository.saveAll(runs);
        assertEquals(4, repository.count());
    }

    @Test
    void shouldFindByLocationIndoor() {
        repository.create(new Run(3,
                "4th run",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1).plusHours(2),
                8,
                Location.OUTDOOR,
                null)
        );
        List<Run> indoor = repository.findByLocation(Location.INDOOR.name());
        assertEquals(3, repository.count());
        assertEquals(1, indoor.size());
    }

    @Test
    void shouldFindByLocationOutdoor() {
        repository.create(new Run(3,
                "4th run",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1).plusHours(2),
                8,
                Location.OUTDOOR,
                null)
        );
        List<Run> indoor = repository.findByLocation(Location.OUTDOOR.name());
        assertEquals(3, repository.count());
        assertEquals(2, indoor.size());
    }

}