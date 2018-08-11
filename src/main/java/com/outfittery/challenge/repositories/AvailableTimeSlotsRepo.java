package com.outfittery.challenge.repositories;

import com.outfittery.challenge.models.AvailableTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableTimeSlotsRepo extends JpaRepository<AvailableTimeSlot, Long> {
}
