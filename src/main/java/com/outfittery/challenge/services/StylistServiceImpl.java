package com.outfittery.challenge.services;

import com.outfittery.challenge.exceptions.ResourceNotFoundException;
import com.outfittery.challenge.models.*;
import com.outfittery.challenge.repositories.LeaveRepo;
import com.outfittery.challenge.repositories.ReservationRepo;
import com.outfittery.challenge.repositories.StylistRepo;
import com.outfittery.challenge.rest.dto.LeaveRequest;
import com.outfittery.challenge.rest.dto.ReservationRequest;
import com.outfittery.challenge.rest.dto.builder.ReservationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StylistServiceImpl implements StylistService {

    private Logger logger = LoggerFactory.getLogger(StylistServiceImpl.class);

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
        logger.info("stylist id: " + id);
        return stylistRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stylist not found in db", id));
    }

    @Override @Transactional
    public Stylist save(Stylist stylist) {
        logger.info("Stylist to save: " + stylist);
        return stylistRepo.save(stylist);
    }

    @Override @Transactional
    public Long updateState(Long stylistId) {
        logger.info("stylist to change state to ready for style: " + stylistId);
        Stylist fromDB = stylistRepo.findById(stylistId)
                .orElseThrow(() -> new ResourceNotFoundException("Stylist not found in db", stylistId));
        fromDB.setStylistState(StylistState.READY_TO_STYLE);
        return stylistRepo.save(fromDB).getId();
    }

    @Override @Transactional
    public boolean delete(Long id) {
        logger.info("delete stylist id: " + id);
        stylistRepo.deleteById(id);
        return true;
    }

    /**
     * perform termporary(holiday or sick leave) or permanent leave for stylist
     * @param leaveRequest
     * @return
     */
    @Override
    @Transactional
    public boolean requestForLeave(LeaveRequest leaveRequest) {
        logger.info("leave request for stylist: " + leaveRequest);
        List<Reservation> reservationList = null;
        //if stylist leaving company all reservations forward to other stylist
        if (leaveRequest.getLeaveType() == LeaveType.LEFT_COMPANY) {
            reservationList = reservationRepo.findByStylistIdAndDateGreaterThanEqual(leaveRequest.getStylistId(), leaveRequest.getBegin());
            Stylist stylist = stylistRepo.findById(leaveRequest.getStylistId())
                    .orElseThrow(() -> {
                        return new ResourceNotFoundException("Stylist not found in db", leaveRequest.getStylistId());
                    });
            stylist.setStylistState(StylistState.OFF_BOARDED);
            stylistRepo.save(stylist);
        } else {
            //if temprary leave only reservations between leave period will be forwarded to other stylists
            reservationList = reservationRepo.findByStylistIdAndDateBetween(leaveRequest.getStylistId(), leaveRequest.getBegin(), leaveRequest.getEnd());
        }

        reservationRepo.deleteAll(reservationList);

        Leave leave = new Leave(leaveRequest.getBegin(), leaveRequest.getEnd(), new Stylist(leaveRequest.getStylistId()), leaveRequest.getLeaveType());
        leaveRepo.save(leave);

        for (Reservation reservation : reservationList) {
            ReservationRequest reservationRequest = ReservationBuilder.buildReservationRequest(reservation);
            reservationService.makeReservation(reservationRequest);
        }
        return true;
    }
}
