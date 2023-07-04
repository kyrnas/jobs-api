package com.olek.jobs.repository;

import com.olek.jobs.model.job.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {
    Page<Job> findAll(Pageable pageable);


    String QUERY = "SELECT DISTINCT JOB_ID as ID, APPLY_URL, CREATED_DATE_TIME, DESCRIPTION, IS_ACTIVE, TITLE, TYPE, UPDATED_DATE_TIME, SALARY FROM"+
            " (SELECT j.ID JOB_ID, j.APPLY_URL, j.CREATED_DATE_TIME, j.DESCRIPTION, j.IS_ACTIVE, j.TITLE, j.TYPE, j.UPDATED_DATE_TIME, j.SALARY, tag.TAGS," +
            " jl.LOCATION_ID, jl.LATITUDE, jl.LONGITUDE, l.CITY, l.COUNTRY, l.DISPLAY_NAME, l.NAME CITY_NAME, jt.TECHNOLOGY_ID, jt.IMPORTANCE, t.NAME TECHNOLOGY_NAME" +
            " FROM JOB j LEFT JOIN TAGS tag ON j.ID=tag.JOB_ID LEFT JOIN JOB_LOCATION jl ON j.ID=jl.JOB_ID LEFT JOIN LOCATION l ON jl.LOCATION_ID=l.ID" +
            " LEFT JOIN JOB_TECHNOLOGY jt ON j.ID=jt.JOB_ID LEFT JOIN TECHNOLOGY t ON jt.TECHNOLOGY_ID=t.ID" +
            " WHERE j.IS_ACTIVE = :isActiveFilter AND (:tagFilter IS NULL OR UPPER(tag.TAGS) IN (:tagFilter)) AND j.TYPE IN (:typeFilter)" +
            " AND (:technologyFilter IS NULL OR UPPER(t.NAME) IN (:technologyFilter))" +
            " AND (:locationFilter IS NULL OR UPPER(l.CITY) IN (:locationFilter) OR UPPER(l.COUNTRY) IN (:locationFilter) OR UPPER(l.DISPLAY_NAME) IN (:locationFilter) OR UPPER(l.NAME) IN (:locationFilter))" +
            " GROUP BY j.ID, tag.TAGS, jl.LOCATION_ID, jt.TECHNOLOGY_ID)";
    String COUNT_QUERY = "SELECT count(*) FROM (SELECT DISTINCT JOB_ID as ID, APPLY_URL, CREATED_DATE_TIME, DESCRIPTION, IS_ACTIVE, TITLE, TYPE, UPDATED_DATE_TIME, SALARY FROM"+
            " (SELECT j.ID JOB_ID, j.APPLY_URL, j.CREATED_DATE_TIME, j.DESCRIPTION, j.IS_ACTIVE, j.TITLE, j.TYPE, j.UPDATED_DATE_TIME, j.SALARY, tag.TAGS," +
            " jl.LOCATION_ID, jl.LATITUDE, jl.LONGITUDE, l.CITY, l.COUNTRY, l.DISPLAY_NAME, l.NAME CITY_NAME, jt.TECHNOLOGY_ID, jt.IMPORTANCE, t.NAME TECHNOLOGY_NAME" +
            " FROM JOB j LEFT JOIN TAGS tag ON j.ID=tag.JOB_ID LEFT JOIN JOB_LOCATION jl ON j.ID=jl.JOB_ID LEFT JOIN LOCATION l ON jl.LOCATION_ID=l.ID" +
            " LEFT JOIN JOB_TECHNOLOGY jt ON j.ID=jt.JOB_ID LEFT JOIN TECHNOLOGY t ON jt.TECHNOLOGY_ID=t.ID" +
            " WHERE j.IS_ACTIVE = :isActiveFilter AND (tag.TAGS IN (:tagFilter) OR tag.TAGS IS NULL) AND j.TYPE IN (:typeFilter)" +
            " AND (:technologyFilter IS NULL OR UPPER(t.NAME) IN (:technologyFilter))" +
            " AND (:locationFilter IS NULL OR UPPER(l.CITY) IN (:locationFilter) OR UPPER(l.COUNTRY) IN (:locationFilter) OR UPPER(l.DISPLAY_NAME) IN (:locationFilter) OR UPPER(l.NAME) IN (:locationFilter))" +
            " GROUP BY j.ID, jl.LOCATION_ID, jt.TECHNOLOGY_ID))";
    @Query(value = QUERY, countQuery = COUNT_QUERY, nativeQuery = true)
    Page<Job> findWithFilters(Boolean isActiveFilter, List<String> tagFilter, List<String> typeFilter, List<String> technologyFilter, List<String> locationFilter, Pageable pageable);
}
