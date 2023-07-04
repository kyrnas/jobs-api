package com.olek.jobs.model.job;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Boolean isActive;

    @NotNull(message = "title cannot be empty")
    @Size(min=5, max=50)
    private String title;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "job_id"))
    private List<String> tags;

    @NotNull(message = "description cannot be empty")
    @Size(min=15, max=10000)
    private String description;

    @NotNull(message = "type cannot be empty. Valid type \"Full Time\", \"Part Time\", \"Internship\"")
    @Pattern(regexp = "(Full Time)|(Part Time)|(Internship)")
    private String type;

    @Size(max=15)
    private String salary;

    @Size(max=200)
    private String applyUrl;

    @OneToMany(mappedBy = "job")
    private List<JobTechnology> technologies;

    @OneToMany(mappedBy = "job")
    private List<JobLocation> locations;

    @CreationTimestamp
    private java.sql.Timestamp createdDateTime;

    @UpdateTimestamp
    private java.sql.Timestamp updatedDateTime;
}
