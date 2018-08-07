package com.outfittery.challenge.rest.dto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public class ReservationRequest {
    private Long reservationId;
    @NotNull(message = "customerId haven`t been set")
    private Long customerId;
    @NotBlank(message = "time slot can not be blank")
    @Pattern(regexp="^([0-2][0-9]:[0|3]0)", message = "invalid time slot format")
    private String timeSlot;
    @FutureOrPresent(message = "Date should be present or future")
    private LocalDate date;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
