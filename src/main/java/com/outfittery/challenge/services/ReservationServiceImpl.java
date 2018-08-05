package com.outfittery.challenge.services;

import com.outfittery.challenge.helper.ReservationHelper;
import com.outfittery.challenge.models.*;
import com.outfittery.challenge.repositories.*;
import com.outfittery.challenge.rest.dto.ReservationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepo reservationRepo;

    @Autowired
    private AvailableTimeSlotRepo availableTimeSlotsRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private TimeSlotRepo timeSlotRepo;

    @Autowired
    private StylistRepo stylistRepo;

    @Override
    @Transactional
    public Reservation makeReservation(ReservationRequest reservationRequest) {
        Customer customer = customerRepo.findById(reservationRequest.getCustomerId())
                .orElseThrow(() -> {
                    return new RuntimeException("Customer not found");
                });
        TimeSlot timeSlot = timeSlotRepo.findByTime(reservationRequest.getTimeSlot())
                .orElseThrow(() -> {
                    return new RuntimeException("Invalid time entered");
                });
        VAvailableTimeSlot availableTimeSlot = availableTimeSlotsRepo.findAllFreeTimeSlotsByDateAndTime(reservationRequest.getDate(), reservationRequest.getTimeSlot())
                .orElseThrow(() -> {
                    return new RuntimeException("Time slot not available.");
                });

        Stylist stylist = stylistRepo.findById(ReservationHelper
                .getFreeStylist(availableTimeSlot.getBusyStylistIds(), availableTimeSlot.getAllStylistIds()))
                .orElseThrow(() -> {
                    return new RuntimeException("Invalid stylist id");
                });

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setStylist(stylist);
        reservation.setDate(reservationRequest.getDate());
        reservation.setTimeSlot(timeSlot);
        reservationRepo.save(reservation);
        return reservation;
    }

    @Override
    @Transactional
    public boolean makeManyReservations(List<ReservationRequest> reservationRequestList) {
        try {
            reservationRequestList.stream().forEach(this::makeReservation);
            return true;
        } catch (RuntimeException re) {
            return false;
        }
    }

    @Override
    public List<String> getTimeSlots(LocalDate date) {
        List<VAvailableTimeSlot> availableTimeSlots = availableTimeSlotsRepo.findAllFreeTimeSlotsByDate(date);
        if (availableTimeSlots != null) {
            return availableTimeSlots.stream().map(VAvailableTimeSlot::getTime).collect(Collectors.toList());
        }
        return null;
    }
}
