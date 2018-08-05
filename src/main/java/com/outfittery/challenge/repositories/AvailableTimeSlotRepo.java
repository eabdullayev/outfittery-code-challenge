package com.outfittery.challenge.repositories;

import com.outfittery.challenge.models.VAvailableTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AvailableTimeSlotRepo extends JpaRepository<VAvailableTimeSlot, String> {

    List<VAvailableTimeSlot> findAllFreeTimeSlotsByDate(LocalDate date);

    Optional<VAvailableTimeSlot> findAllFreeTimeSlotsByDateAndTime(LocalDate date, String time);
}
