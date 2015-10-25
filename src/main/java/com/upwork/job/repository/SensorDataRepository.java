package com.upwork.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.upwork.job.entity.SensorData;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {

}
