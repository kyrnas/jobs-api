package com.olek.jobs.repository;

import com.olek.jobs.model.job.JobTechnology;
import com.olek.jobs.model.job.JobTechnologyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTechnologyRepository extends JpaRepository<JobTechnology, JobTechnologyKey> {
}
