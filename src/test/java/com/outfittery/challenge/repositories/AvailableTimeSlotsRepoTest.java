package com.outfittery.challenge.repositories;

import com.outfittery.challenge.models.AvailableTimeSlot;
import com.outfittery.challenge.models.TimeSlot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AvailableTimeSlotsRepoTest {

    @Autowired
    private AvailableTimeSlotsRepo availableTimeSlotsRepo;

    @Autowired
    private TimeSlotRepo timeSlotRepo;

    @Before
    public void init() {
        TimeSlot timeSlot = timeSlotRepo.findByTime("12:00").get();
        AvailableTimeSlot availableTimeSlot = new AvailableTimeSlot();
        availableTimeSlot.setDate(LocalDate.now());
        availableTimeSlot.setTimeSlot(timeSlot);
        availableTimeSlot.setAvailableStylists(Arrays.asList(1L, 2L));
        availableTimeSlotsRepo.save(availableTimeSlot);
    }

    @Test
    public void test_findAllTimeSlotsByDate() {
        List<AvailableTimeSlot> allTimeSlotsByDate = availableTimeSlotsRepo.findAllTimeSlotsByDate(LocalDate.now());
        assertNotNull(allTimeSlotsByDate);
        assertEquals(1, allTimeSlotsByDate.size());
        assertEquals("12:00", allTimeSlotsByDate.get(0).getTimeSlot().getTime());
    }

    @Test
    public void test_findByDateAndTime() {
        TimeSlot timeSlot = timeSlotRepo.findByTime("12:00").get();
        AvailableTimeSlot availableTimeSlot = availableTimeSlotsRepo.findByDateAndTimeSlot(LocalDate.now(), timeSlot).get();
        assertNotNull(availableTimeSlot);
        assertEquals(2, availableTimeSlot.getAvailableStylists().size());
    }
}