package com.outfittery.challenge.services;

import java.time.LocalDate;

public interface AvailableTimeSlotService {
    void cacheTimeSlots();
    void cacheTimeSlotsByDate(LocalDate date);
}
