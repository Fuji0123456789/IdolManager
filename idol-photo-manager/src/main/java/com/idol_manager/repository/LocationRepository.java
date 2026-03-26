package com.idol_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idol_manager.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

}
