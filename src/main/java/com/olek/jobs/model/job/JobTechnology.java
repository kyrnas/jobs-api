package com.olek.jobs.model.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Entity
@IdClass(JobTechnologyKey.class)
public class JobTechnology {
    @Id
    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    @JsonIgnore
    private Job job;

    @Id
    @ManyToOne
    @JoinColumn(name = "technology_id", referencedColumnName = "id")
    private Technology technology;

    @Min(1)
    @Max(5)
    private int importance;
}
