package com.outfittery.challenge.rest.dto;

import java.time.LocalDate;

public class ReservationResponse {
    private Long customerId;
    private String customerName;
    private String timeSlot;
    private LocalDate date;
    private String stylist;

    public ReservationResponse() {
    }

    public ReservationResponse(Long customerId, String customerName, String timeSlot, LocalDate date, String stylist) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.timeSlot = timeSlot;
        this.date = date;
        this.stylist = stylist;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getStylist() {
        return stylist;
    }

    public void setStylist(String stylist) {
        this.stylist = stylist;
    }
}
