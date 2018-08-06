package com.outfittery.challenge.services;

import com.outfittery.challenge.models.*;
import com.outfittery.challenge.repositories.LeaveRepo;
import com.outfittery.challenge.repositories.ReservationRepo;
import com.outfittery.challenge.repositories.StylistRepo;
import com.outfittery.challenge.rest.dto.ReservationRequest;
import com.outfittery.challenge.rest.dto.builder.ReservationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StylistServiceImpl implements StylistService {
    @Autowired
    private StylistRepo stylistRepo;

    @Autowired
    private ReservationRepo reservationRepo;

    @Autowired
    private LeaveRepo leaveRepo;

    @Autowired
    private ReservationService reservationService;

    @Override
    public Stylist getById(Long id) {
        return stylistRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stylist with Id=" + id + " not found in db"));
    }

    @Override @Transactional
    public Stylist save(Stylist stylist) {
        return stylistRepo.save(stylist);
    }

    @Override @Transactional
    public Long updateState(Long stylistId) {
        Stylist fromDB = stylistRepo.findById(stylistId)
                .orElseThrow(() -> new RuntimeException("Stylist with Id=" + stylistId + " not found in db"));
        fromDB.setStylistState(StylistState.READY_TO_STYLE);
        return stylistRepo.save(fromDB).getId();
    }

    @Override @Transactional
    public boolean delete(Long id) {
        stylistRepo.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public boolean requestForLeave(Leave leave) {
        List<Reservation> reservationList = null;
        if (leave.getLeaveType() == LeaveType.LEFT_COMPANY) {
            reservationList = reservationRepo.findByStylistIdAndDateGreaterThanEqual(leave.getStylist().getId(), leave.getBegin());
            Stylist stylist = stylistRepo.findById(leave.getStylist().getId())
                    .orElseThrow(() -> {
                        return new RuntimeException("Stylist with id=" + leave.getStylist().getId()+ " not found in db");
                    });
            stylist.setStylistState(StylistState.OFF_BOARDED);
            stylistRepo.save(stylist);
        } else {
            reservationList = reservationRepo.findByStylistIdAndDateBetween(leave.getStylist().getId(), leave.getBegin(), leave.getEnd());
        }

        reservationRepo.deleteAll(reservationList);
        leaveRepo.save(leave);

        for (Reservation reservation : reservationList) {
            ReservationRequest reservationRequest = ReservationBuilder.buildReservationRequest(reservation);
            reservationService.makeReservation(reservationRequest);
        }
        return true;
    }
}
