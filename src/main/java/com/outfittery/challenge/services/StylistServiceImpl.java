package com.outfittery.challenge.services;

import com.outfittery.challenge.models.Leave;
import com.outfittery.challenge.models.Stylist;
import com.outfittery.challenge.repositories.ReservationRepo;
import com.outfittery.challenge.repositories.StylistRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StylistServiceImpl implements StylistService {
    @Autowired
    private StylistRepo stylistRepo;

    @Autowired
    private ReservationRepo reservationRepo;

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
    public boolean requestForLeave(Leave leave) {
        reservationRepo.findByStylistIdAndDateBetween(leave.getStylist().getId(), leave.getBegin(), leave.getEnd());
        return false;
    }
}
