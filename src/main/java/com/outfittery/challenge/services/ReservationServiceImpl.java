package com.outfittery.challenge.services;

import com.outfittery.challenge.helper.ReservationHelper;
import com.outfittery.challenge.models.*;
import com.outfittery.challenge.repositories.*;
import com.outfittery.challenge.rest.dto.ManyReservationResponse;
import com.outfittery.challenge.rest.dto.ReservationRequest;
import com.outfittery.challenge.rest.dto.ReservationResponse;
import com.outfittery.challenge.rest.dto.builder.ReservationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
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
    public ReservationResponse makeReservation(ReservationRequest reservationRequest) {
        Customer customer = customerRepo.findById(reservationRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<Reservation> customerReservations = reservationRepo.findByCustomerIdAndDateGreaterThanEqual(customer.getId(), LocalDate.now());

        Reservation r = null;
        if ((r = ReservationHelper.hasBooking(customerReservations)) != null) {
            throw new RuntimeException("You already have reservation at " + r.getDate() + " " + r.getTimeSlot().getTime());
        }

        TimeSlot timeSlot = timeSlotRepo.findByTime(reservationRequest.getTimeSlot())
                .orElseThrow(() -> new RuntimeException("Invalid time entered"));
        VAvailableTimeSlot availableTimeSlot = availableTimeSlotsRepo.findAllFreeTimeSlotsByDateAndTime(reservationRequest.getDate(), reservationRequest.getTimeSlot())
                .orElseThrow(() -> new RuntimeException("Time slot not available."));

        Stylist stylist = stylistRepo.getOne(ReservationHelper
                .getFreeStylist(availableTimeSlot.getBusyStylistIds(), availableTimeSlot.getAllStylistIds()));

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setStylist(stylist);
        reservation.setDate(reservationRequest.getDate());
        reservation.setTimeSlot(timeSlot);
        reservationRepo.save(reservation);

        return ReservationBuilder.buildReservationResponse(reservation);
    }

    @Override @Transactional
    public ReservationResponse updateReservation(ReservationRequest reservationRequest) {
        if (reservationRequest.getReservationId() == null) {
            throw new RuntimeException("reservation not selected.");
        }

        Reservation r = reservationRepo.findById(reservationRequest.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (r.getDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Old reservations can not be changed");
        }

        if (r.getCustomer().getId() != reservationRequest.getCustomerId()) {
            throw new RuntimeException("invalid operation. reservation not belong to customer");
        }

        TimeSlot timeSlot = timeSlotRepo.findByTime(reservationRequest.getTimeSlot())
                .orElseThrow(() -> new RuntimeException("Invalid time entered"));
        VAvailableTimeSlot availableTimeSlot = availableTimeSlotsRepo.findAllFreeTimeSlotsByDateAndTime(reservationRequest.getDate(), reservationRequest.getTimeSlot())
                .orElseThrow(() -> new RuntimeException("Time slot not available."));

        Stylist stylist = stylistRepo.getOne(ReservationHelper
                .getFreeStylist(availableTimeSlot.getBusyStylistIds(), availableTimeSlot.getAllStylistIds()));

        r.setStylist(stylist);
        r.setDate(reservationRequest.getDate());
        r.setTimeSlot(timeSlot);
        reservationRepo.save(r);

        return ReservationBuilder.buildReservationResponse(r);
    }

    @Override
    @Transactional
    public ManyReservationResponse makeManyReservations(List<ReservationRequest> reservationRequestList) {
        ManyReservationResponse response = new ManyReservationResponse();
        for (ReservationRequest request : reservationRequestList) {
            try {
                ReservationResponse rr = this.makeReservation(request);
                response.getProcessedReservations().add(rr);
            } catch (RuntimeException re) {
                response.getFailedReservations().add(request);
            }
        }
        return response;
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
