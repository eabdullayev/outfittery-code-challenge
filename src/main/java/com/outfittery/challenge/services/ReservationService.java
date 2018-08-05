package com.outfittery.challenge.services;

import com.outfittery.challenge.models.Reservation;
import com.outfittery.challenge.rest.dto.ReservationRequest;
import com.outfittery.challenge.rest.dto.ReservationResponse;

import java.time.LocalDate;
import java.util.List;


public interface ReservationService {

    ReservationResponse makeReservation(ReservationRequest reservationRequest);

    boolean makeManyReservations(List<ReservationRequest> reservationRequestList);

    List<String> getTimeSlots(LocalDate date);
}
