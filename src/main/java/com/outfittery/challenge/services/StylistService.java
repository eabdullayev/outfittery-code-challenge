package com.outfittery.challenge.services;

import com.outfittery.challenge.models.Leave;
import com.outfittery.challenge.models.Stylist;
import com.outfittery.challenge.models.StylistState;
import com.outfittery.challenge.rest.dto.LeaveRequest;

public interface StylistService {
    Stylist getById(Long id);

    Stylist save(Stylist stylist);

    boolean delete(Long id);

    boolean requestForLeave(LeaveRequest leave);

    Long updateState(Long stylistId);
}
