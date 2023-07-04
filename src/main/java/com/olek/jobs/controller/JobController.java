package com.olek.jobs.controller;

import com.olek.jobs.model.job.Job;
import com.olek.jobs.repository.JobLocationRepository;
import com.olek.jobs.repository.JobRepository;
import com.olek.jobs.repository.JobTechnologyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class JobController {
    // TODO create a service layer instead of writing logic inside the controller
    @Autowired
    JobRepository jobRepository;

    @Autowired
    JobTechnologyRepository jobTechnologyRepository;

    @Autowired
    JobLocationRepository jobLocationRepository;

    @Value("${server.servlet.context-path}")
    String apiContext;

    @GetMapping("/jobsAll")
    public ResponseEntity<List<Job>> getAllJobs() {
        log.info("Called getAllJobs");
        List<Job> jobs = jobRepository.findAll();

        return new ResponseEntity<List<Job>>(jobs, HttpStatus.OK);
    }

    @GetMapping(value = "/job/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable UUID id) {
        Job job = jobRepository.findById(id).get();
        return new ResponseEntity<Job>(job, HttpStatus.OK);
    }

    @PostMapping(value = "/job", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Job> postNewJob(@RequestBody Job newJob) {
        log.info("Called postNewJob with body: {}", newJob);
        newJob.setIsActive(true);
        final Job newJobInstance = jobRepository.save(newJob);

        // Create an association between this job and existing technology
        final Job emptyJob = new Job();
        emptyJob.setId(newJobInstance.getId());
        newJobInstance.getTechnologies().forEach((jobTechnology) -> {
            jobTechnology.setJob(emptyJob);
            log.info("Added a new join value: {}", jobTechnology);
            jobTechnologyRepository.save(jobTechnology);
        });

        // Create an association between this job and existing location
        newJobInstance.getLocations().forEach((jobLocation) -> {
            jobLocation.setJob(emptyJob);
            log.info("Added a new join value: {}", jobLocation);
            jobLocationRepository.save(jobLocation);
        });


        log.info("New job with associations: {}", newJobInstance);
        Job returnJobInstance = jobRepository.findById(newJobInstance.getId()).get(); // so that we return many-to-many relationships too

        return ResponseEntity.created(URI.create(apiContext + "/" + returnJobInstance.getId().toString())).body(returnJobInstance);
    }

    // TODO test that this even works with technology and locations FK
    // upd: it doesn't
    @PutMapping(value = "/job", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Job> updateExistingJob(@RequestBody Job updatedJob) {
        log.info("Called updateExistingJob with body: {}", updatedJob);
        Optional<Job> job = jobRepository.findById(updatedJob.getId());

        if (job.isPresent()){
            updatedJob.setTechnologies(null);
            updatedJob.setLocations(null);
            jobRepository.save(updatedJob);
        }
        else {
            // TODO create exception handlers
            throw new NullPointerException("Did not find an existing object with id " + updatedJob.getId());
        }
        return ResponseEntity.created(URI.create(apiContext + "/" + updatedJob.getId().toString())).body(updatedJob);
    }

    // TODO pagable retrieve with filters and keywords
    @GetMapping(value = "/jobs")
    public ResponseEntity<Page<Job>> getJobs(
            @RequestParam(defaultValue = "true") boolean isActive,
            @RequestParam(required = false) String[] tags,
            @RequestParam(defaultValue = "Internship, Full Time, Part Time") String[] type,
            @RequestParam(required = false) String[] technologies,
            @RequestParam(required = false) String[] locations,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize
    ){
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        List<String> tagsFilter = preprocessFilter(tags);
        List<String> technologyFilter = preprocessFilter(technologies);
        List<String> locationFilter = preprocessFilter(locations);

        Page<Job> page = jobRepository.findWithFilters(isActive, tagsFilter, Arrays.asList(type), technologyFilter, locationFilter, pageable);
        return new ResponseEntity<Page<Job>>(page, HttpStatus.OK);
    }

    private List<String> preprocessFilter(String[] filter) {
        List<String> locationFilter = null;
        if (filter != null){
            locationFilter = Arrays.asList(filter);
            for (int i=0; i<locationFilter.size(); i++){
                locationFilter.set(i, locationFilter.get(i).toUpperCase());
            }
        }
        return locationFilter;
    }

    // TODO close job posting
    @DeleteMapping(value = "/job/{id}")
    public ResponseEntity<Job> archiveExistingJob(@PathVariable UUID id) {
        Optional<Job> job = jobRepository.findById(id);

        if (job.isPresent()){
            job.get().setIsActive(false);
            jobRepository.save(job.get());
        }
        else {
            // TODO create exception handlers
            throw new NullPointerException("Did not find an existing object with id " + id);
        }

        return new ResponseEntity<Job>(job.get(), HttpStatus.OK);
    }
}
