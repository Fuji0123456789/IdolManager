package com.idol_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idol_manager.entity.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {

}
