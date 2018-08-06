package com.outfittery.challenge.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class ManyReservationResponse {

    private List<ReservationResponse> processedReservations = new ArrayList<>();

    private List<ReservationRequest> failedReservations = new ArrayList<>();

    public List<ReservationResponse> getProcessedReservations() {
        return processedReservations;
    }

    public void setProcessedReservations(List<ReservationResponse> processedReservations) {
        this.processedReservations = processedReservations;
    }

    public List<ReservationRequest> getFailedReservations() {
        return failedReservations;
    }

    public void setFailedReservations(List<ReservationRequest> failedReservations) {
        this.failedReservations = failedReservations;
    }
}
