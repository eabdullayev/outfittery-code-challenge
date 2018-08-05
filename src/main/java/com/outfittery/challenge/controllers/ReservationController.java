package com.outfittery.challenge.controllers;

import com.outfittery.challenge.models.Reservation;
import com.outfittery.challenge.rest.dto.ReservationRequest;
import com.outfittery.challenge.rest.dto.ReservationResponse;
import com.outfittery.challenge.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/available-time-slot")
    public List<String> getAvailableTimeSlot(@RequestParam(name = "date")
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                             LocalDate date) {
        //todo implement validator
        return reservationService.getTimeSlots(date);
    }

    @PostMapping("/make-reservation")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse makeReservation(@RequestBody ReservationRequest request) {
        //todo implement validation
        return reservationService.makeReservation(request);
    }

    @PostMapping("/make-many-reservation")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean makeManyReservation(@RequestBody List<ReservationRequest> request) {
        //todo implement validation
        return reservationService.makeManyReservations(request);
    }
}
