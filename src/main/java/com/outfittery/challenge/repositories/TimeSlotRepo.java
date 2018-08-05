package com.outfittery.challenge.repositories;

import com.outfittery.challenge.models.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeSlotRepo extends JpaRepository<TimeSlot, Long> {
    Optional<TimeSlot> findByTime(String time);
}
