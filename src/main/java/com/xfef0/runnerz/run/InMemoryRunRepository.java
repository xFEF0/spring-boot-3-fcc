package com.xfef0.runnerz.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class InMemoryRunRepository {

    private final static Logger log = LoggerFactory.getLogger(InMemoryRunRepository.class);
    private final List<Run> runs = new ArrayList<>();

    public List<Run> findAll() {
        return runs;
    }

    public Optional<Run> findById(Integer id) {
        return Optional.ofNullable(runs.stream()
                .filter(run -> Objects.equals(run.id(), id))
                .findFirst()
                .orElseThrow(RunNotFoundException::new));
    }

    public void create(Run run) {
        Run newRun = new Run(run.id(),
                run.title(),
                run.startedOn(),
                run.completedOn(),
                run.kilometers(),
                run.location(),
                null);
        runs.add(newRun);
    }

    public void update(Run newRun, Integer id) {
        Optional<Run> existingRun = findById(id);
        if (existingRun.isPresent()) {
            var updateRun = existingRun.get();
            log.info("Updating Existing Run: {}", id);
            runs.set(runs.indexOf(updateRun), newRun);
        }
    }

    public void delete(Integer id) {
        log.info("Deleting Run: {}", id);
        runs.removeIf(run -> Objects.equals(run.id(), id));
    }

    public int count() {
        return runs.size();
    }

    public void saveAll(List<Run> newRuns) {
        newRuns.forEach(this::create);
    }

    public List<Run> findByLocation(String location) {
        return runs.stream()
                .filter(run -> Objects.equals(run.location().name(), location))
                .toList();
    }
}
