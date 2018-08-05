package com.outfittery.challenge.services;

import com.outfittery.challenge.models.Reservation;
import com.outfittery.challenge.rest.dto.ReservationRequest;

import java.time.LocalDate;
import java.util.List;


public interface ReservationService {

    Reservation makeReservation(ReservationRequest reservationRequest);

    boolean makeManyReservations(List<ReservationRequest> reservationRequestList);

    List<String> getTimeSlots(LocalDate date);
}
