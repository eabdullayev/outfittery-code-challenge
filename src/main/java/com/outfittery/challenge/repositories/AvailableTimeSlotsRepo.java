package com.outfittery.challenge.repositories;

import com.outfittery.challenge.models.AvailableTimeSlot;
import com.outfittery.challenge.models.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AvailableTimeSlotsRepo extends JpaRepository<AvailableTimeSlot, Long> {
    List<AvailableTimeSlot> findAllTimeSlotsByDate(LocalDate date);

    Optional<AvailableTimeSlot> findByDateAndTimeSlot(LocalDate date, TimeSlot timeSlot);
}
