package com.olek.jobs.repository;

import com.olek.jobs.model.job.JobLocation;
import com.olek.jobs.model.job.JobLocationKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobLocationRepository extends JpaRepository<JobLocation, JobLocationKey> {
}
