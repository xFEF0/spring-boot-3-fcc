package com.xfef0.runnerz.run;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RunRepository {

    private List<Run> runs = new ArrayList<>();

    List<Run> findAll() {
        return runs;
    }

    Optional<Run> findById(Integer id) {
        return runs.stream()
                .filter(run -> Objects.equals(run.id(), id))
                .findFirst();
    }

    void create(Run run) {
        runs.add(run);
    }

    void update(Run run, Integer id) {
        findById(id)
                .ifPresent(existingRun -> runs.set(runs.indexOf(existingRun), run));
    }

    void delete(Integer id) {
        runs.removeIf(run -> run.id().equals(id));
    }

    @PostConstruct
    private void init() {
        runs.add(new Run(1, "Monday evening run", LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(3).plusMinutes(45), 5, Location.OUTDOOR));
        runs.add(new Run(2, "Wednesday evening run", LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1).plusMinutes(70), 6, Location.INDOOR));

    }
}
