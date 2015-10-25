package com.upwork.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.upwork.job.entity.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

	public Sensor findBySensorUUID(String sensorUUID);
}
