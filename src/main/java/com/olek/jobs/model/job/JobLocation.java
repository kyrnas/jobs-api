package com.olek.jobs.model.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
@Entity
@IdClass(JobLocationKey.class)
public class JobLocation {
    @Id
    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    @JsonIgnore
    private Job job;

    @Id
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private float latitude;

    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private float longitude;
}
