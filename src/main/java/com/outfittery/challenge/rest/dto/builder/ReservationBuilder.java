package com.outfittery.challenge.rest.dto.builder;

import com.outfittery.challenge.models.Reservation;
import com.outfittery.challenge.rest.dto.ReservationRequest;
import com.outfittery.challenge.rest.dto.ReservationResponse;

/**
 * used for creating ReservationResponse,ReservationRequest from Reservation or vice versa
 */
public class ReservationBuilder {
    public static ReservationResponse buildReservationResponse(Reservation reservation) {
        ReservationResponse reservationResponse = new ReservationResponse();
        reservationResponse.setCustomerId(reservation.getCustomer().getId());
        reservationResponse.setCustomerName(reservation.getCustomer().getName());
        reservationResponse.setDate(reservation.getDate());
        reservationResponse.setTimeSlot(reservation.getTimeSlot().getTime());
        reservationResponse.setStylist(reservation.getStylist().getName());
        return reservationResponse;
    }

    public static ReservationRequest buildReservationRequest(Reservation reservation) {
        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setCustomerId(reservation.getCustomer().getId());
        reservationRequest.setDate(reservation.getDate());
        reservationRequest.setTimeSlot(reservation.getTimeSlot().getTime());
        return reservationRequest;
    }
}
