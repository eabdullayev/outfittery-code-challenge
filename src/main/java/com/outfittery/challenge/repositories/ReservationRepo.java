package com.outfittery.challenge.repositories;

import com.outfittery.challenge.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepo extends JpaRepository<Reservation, Long> {

    List<Reservation> findByStylistIdAndDateBetween(Long stylistId, LocalDate start, LocalDate end);

    List<Reservation> findByStylistIdAndDateGreaterThanEqual(Long stylistId, LocalDate start);

    List<Reservation> findByCustomerIdAndDateGreaterThanEqual(Long customerId, LocalDate start);
}
