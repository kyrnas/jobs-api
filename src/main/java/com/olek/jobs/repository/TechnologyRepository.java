package com.olek.jobs.repository;

import com.olek.jobs.model.job.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TechnologyRepository extends JpaRepository<Technology, UUID> {
}
