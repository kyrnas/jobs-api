package com.olek.jobs.controller;

import com.olek.jobs.model.job.Location;
import com.olek.jobs.model.job.Technology;
import com.olek.jobs.repository.LocationRepository;
import com.olek.jobs.repository.TechnologyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @Autowired
    TechnologyRepository technologyRepository;

    @Autowired
    LocationRepository locationRepository;

    @Value("${server.servlet.context-path}")
    String apiContext;

    @GetMapping("/technology")
    public ResponseEntity<List<Technology>> getAllTechnology() {
        log.info("Called getAllTechnology");
        List<Technology> technologies = technologyRepository.findAll();

        return new ResponseEntity<List<Technology>>(technologies, HttpStatus.OK);
    }

    @GetMapping("/location")
    public ResponseEntity<List<Location>> getAllLocation() {
        log.info("Called getAllLocation");
        List<Location> locations = locationRepository.findAll();

        return new ResponseEntity<List<Location>>(locations, HttpStatus.OK);
    }

    @PostMapping("/technology")
    public ResponseEntity<Technology> addNewTechnology(@RequestBody Technology newTechnology) {
        log.info("Called addNewTechnology with body: {}", newTechnology);
        Technology newTechnologyInstance = technologyRepository.save(newTechnology);

        return ResponseEntity.created(URI.create(apiContext + "/" + newTechnologyInstance.getId().toString())).body(newTechnologyInstance);
    }

    @PostMapping("/location")
    public ResponseEntity<Location> addNewLocation(@RequestBody Location newLocation) {
        log.info("Called addNewLocation with body: {}", newLocation);
        Location newLocationInstance = locationRepository.save(newLocation);

        return ResponseEntity.created(URI.create(apiContext + "/" + newLocationInstance.getId().toString())).body(newLocationInstance);
    }

    @PutMapping("/technology")
    public ResponseEntity<Technology> updateExistingTechnology(@RequestBody Technology technology) {
        log.info("Called updateExistingTechnology with body: {}", technology);
        try {
            Technology instance = technologyRepository.getReferenceById(technology.getId());
            technologyRepository.save(technology);
        } catch (Exception e) {
            // TODO create exception handlers
            throw new NullPointerException("Did not find an existing object with id " + technology.getId());
        }
        return ResponseEntity.created(URI.create(apiContext + "/" + technology.getId().toString())).body(technology);
    }

    @PutMapping("/location")
    public ResponseEntity<Location> updateExistingLocation(@RequestBody Location location) {
        log.info("Called updateExistingTechnology with body: {}", location);
        try {
            Location instance = locationRepository.getReferenceById(location.getId());
            locationRepository.save(location);
        } catch (Exception e) {
            // TODO create exception handlers
            throw new NullPointerException("Did not find an existing object with id " + location.getId());
        }
        return ResponseEntity.created(URI.create(apiContext + "/" + location.getId().toString())).body(location);
    }

    @DeleteMapping("/technology/{id}")
    public ResponseEntity<Technology> deleteTechnology(@PathVariable UUID id) {
        log.info("Called deleteExistingTechnology with id: {}", id);
        try {
            Technology instance = technologyRepository.getReferenceById(id);
            technologyRepository.delete(instance);
            return ResponseEntity.ok(instance);
        } catch (Exception e) {
            // TODO create exception handlers
            throw new NullPointerException("Did not find an existing object with id " + id);
        }
    }

    @DeleteMapping("/location/{id}")
    public ResponseEntity<Location> deleteLocation(@PathVariable UUID id) {
        log.info("Called deleteExistingLocation with id: {}", id);
        try {
            Location instance = locationRepository.getReferenceById(id);
            locationRepository.delete(instance);
            return ResponseEntity.ok(instance);
        } catch (Exception e) {
            // TODO create exception handlers
            throw new NullPointerException("Did not find an existing object with id " + id);
        }
    }
}
