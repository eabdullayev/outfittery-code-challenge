package com.outfittery.challenge.controllers;

import com.outfittery.challenge.models.Reservation;
import com.outfittery.challenge.rest.dto.ExceptionResponse;
import com.outfittery.challenge.rest.dto.ManyReservationResponse;
import com.outfittery.challenge.rest.dto.ReservationRequest;
import com.outfittery.challenge.rest.dto.ReservationResponse;
import com.outfittery.challenge.services.ReservationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@RequestMapping("api/v1.0/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @ApiOperation(value = "Get available time slots for stylists", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created customer"),
            @ApiResponse(code = 400, message = "Incorrect date entered")
    }
    )
    @GetMapping(value = "/time-slots/available", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAvailableTimeSlot(
                                             @RequestParam(name = "date")
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                             @FutureOrPresent(message = "Date should be present or future")
                                             LocalDate date) {
        return reservationService.getTimeSlots(date);
    }

    @ApiOperation(value = "Make reservation", response = ReservationResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created reservation"),
            @ApiResponse(code = 400, message = "Incorrect data entered"),
            @ApiResponse(code = 404, message = "Entered data not found in DB")
    }
    )
    @PostMapping("/make-reservation")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse makeReservation(@Valid @RequestBody ReservationRequest request) {
        return reservationService.makeReservation(request);
    }

    @ApiOperation(value = "Update reservation", response = ReservationResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully updated reservation"),
            @ApiResponse(code = 400, message = "Incorrect data entered"),
            @ApiResponse(code = 404, message = "Entered data not found in DB")
    }
    )
    @PutMapping("/update-reservation")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse updateReservation(@Valid @RequestBody ReservationRequest request) {
        return reservationService.updateReservation(request);
    }

    @ApiOperation(value = "Add many reservations", response = ManyReservationResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created reservation")
    }
    )
    @PostMapping("/add-many-reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ManyReservationResponse addManyReservations(@Valid @RequestBody List<ReservationRequest> request) {
        return reservationService.makeManyReservations(request);
    }
}
