package com.olek.jobs.model.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
public class JobLocationKey implements Serializable {
    @JsonIgnore
    private UUID job;
    private UUID location;
}
