package com.outfittery.challenge.services;

import com.outfittery.challenge.helper.StylistHelper;
import com.outfittery.challenge.models.AvailableTimeSlot;
import com.outfittery.challenge.models.TimeSlot;
import com.outfittery.challenge.models.VAvailableTimeSlot;
import com.outfittery.challenge.repositories.AvailableTimeSlotsRepo;
import com.outfittery.challenge.repositories.TimeSlotRepo;
import com.outfittery.challenge.repositories.VAvailableTimeSlotRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This class is used to cache time slots per stylist to avoid every time calling joined queries to database.
 * stylist availability will be cached to table to be used for getAvailableTimeSlots and makeReservation apis.
 */
@Service
public class AvailableTimeSlotServiceImpl implements AvailableTimeSlotService {
    private Logger logger = LoggerFactory.getLogger(AvailableTimeSlotServiceImpl.class);

    @Autowired
    private VAvailableTimeSlotRepo vAvailableTimeSlotRepo;
    @Autowired
    private TimeSlotRepo timeSlotRepo;
    @Autowired
    private AvailableTimeSlotsRepo availableTimeSlotsRepo;

    @Value("${days.to.cache}")
    private Integer daysToCache;

    /**
     * will be used by scheduler for caching time slots for number of days from now.
     */
    @Override @Transactional
    @Scheduled(cron = "${schedule.fire.time}")
    public void cacheTimeSlots() {
        logger.info("starting caching of time slots for days: " + daysToCache);
        //first delete all data from available_time_slots table
        availableTimeSlotsRepo.deleteAll();
        //then add new cache
        for (int i = 0; i < daysToCache; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            cacheTimeSlotsByDate(date);
        }
    }

    /**
     * used for caching single day. If requested date not cached already will be invoked this method to cache that day.
     * @param date - will cache this day
     */
    @Override @Transactional
    public void cacheTimeSlotsByDate(LocalDate date) {
        List<VAvailableTimeSlot> availableTimeSlots = vAvailableTimeSlotRepo.findAllFreeTimeSlotsByDate(date);
        for (VAvailableTimeSlot availableTimeSlot : availableTimeSlots) {
            Optional<TimeSlot> optionalTimeSlot = timeSlotRepo.findByTime(availableTimeSlot.getTime());
            if (optionalTimeSlot.isPresent()) {
                TimeSlot timeSlot = optionalTimeSlot.get();
                Set<Long> availableStylists = StylistHelper.getAvailableStylists(availableTimeSlot.getBusyStylistIds(), availableTimeSlot.getAllStylistIds());
                AvailableTimeSlot ats = new AvailableTimeSlot();
                ats.setDate(date);
                ats.setTimeSlot(timeSlot);
                ats.setAvailableStylists(availableStylists);
                availableTimeSlotsRepo.save(ats);
            }else{
                //if no time slot skip this iteration
                logger.info("time slot %s not found in DB, skipping..", availableTimeSlot.getTime());
            }
        }
    }

    public Integer getDaysToCache() {
        return daysToCache;
    }

    public void setDaysToCache(Integer daysToCache) {
        this.daysToCache = daysToCache;
    }
}
