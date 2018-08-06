package com.outfittery.challenge.services;

import com.outfittery.challenge.models.Leave;
import com.outfittery.challenge.models.Stylist;
import com.outfittery.challenge.models.StylistState;

public interface StylistService {
    Stylist getById(Long id);

    Stylist save(Stylist stylist);

    boolean delete(Long id);

    boolean requestForLeave(Leave leave);

    Long updateState(Long stylistId);
}
