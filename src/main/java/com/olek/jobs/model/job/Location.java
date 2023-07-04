package com.olek.jobs.model.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private String country;

    private String city;

    private String displayName;

    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private List<JobLocation> jobs;
}
