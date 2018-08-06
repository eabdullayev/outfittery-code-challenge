package com.outfittery.challenge.controllers;

import com.outfittery.challenge.models.Reservation;
import com.outfittery.challenge.rest.dto.ExceptionResponse;
import com.outfittery.challenge.rest.dto.ManyReservationResponse;
import com.outfittery.challenge.rest.dto.ReservationRequest;
import com.outfittery.challenge.rest.dto.ReservationResponse;
import com.outfittery.challenge.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping(value = "/time-slots/available", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> getAvailableTimeSlot(
                                             @RequestParam(name = "date")
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                             @FutureOrPresent(message = "Date should be present or future")
                                             LocalDate date) {
        return reservationService.getTimeSlots(date);
    }


    @PostMapping("/make-reservation")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse makeReservation(@Valid @RequestBody ReservationRequest request) {
        //todo implement validation
        return reservationService.makeReservation(request);
    }

    @PutMapping("/update-reservation")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse updateReservation(@RequestBody ReservationRequest request) {
        //todo implement validation
        return reservationService.updateReservation(request);
    }

    @PostMapping("/make-many-reservation")
    @ResponseStatus(HttpStatus.CREATED)
    public ManyReservationResponse makeManyReservation(@RequestBody List<ReservationRequest> request) {
        //todo implement validation
        return reservationService.makeManyReservations(request);
    }
}
