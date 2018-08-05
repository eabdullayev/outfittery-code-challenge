package com.outfittery.challenge.services;

import com.outfittery.challenge.models.Leave;
import com.outfittery.challenge.models.Reservation;
import com.outfittery.challenge.models.Stylist;
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
                .orElseThrow(() -> {
                    return new RuntimeException("Stylist with Id=" + id+ " not found in db");
                });
    }

    @Override
    public Stylist save(Stylist stylist) {
        return stylistRepo.save(stylist);
    }

    @Override
    public boolean delete(Long id) {
        stylistRepo.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public boolean requestForLeave(Leave leave) {
        List<Reservation> reservationList = reservationRepo.findByStylistIdAndDateBetween(leave.getStylist().getId(), leave.getBegin(), leave.getEnd());
        leaveRepo.save(leave);
        reservationRepo.deleteAll(reservationList);
        for(Reservation reservation : reservationList){
            ReservationRequest reservationRequest = ReservationBuilder.buildReservationRequest(reservation);
            reservationService.makeReservation(reservationRequest);
        }
        return true;
    }
}
