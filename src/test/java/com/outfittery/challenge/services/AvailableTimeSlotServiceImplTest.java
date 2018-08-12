package com.outfittery.challenge.services;

import com.outfittery.challenge.models.AvailableTimeSlot;
import com.outfittery.challenge.models.TimeSlot;
import com.outfittery.challenge.models.VAvailableTimeSlot;
import com.outfittery.challenge.repositories.AvailableTimeSlotsRepo;
import com.outfittery.challenge.repositories.TimeSlotRepo;
import com.outfittery.challenge.repositories.VAvailableTimeSlotRepo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AvailableTimeSlotServiceImplTest {
    @InjectMocks
    private AvailableTimeSlotServiceImpl availableTimeSlotService;

    @Mock
    private VAvailableTimeSlotRepo vAvailableTimeSlotRepo;

    @Mock
    private AvailableTimeSlotsRepo availableTimeSlotsRepo;

    @Mock
    private TimeSlotRepo timeSlotRepo;

    @Before
    public void setUp() throws Exception {
        availableTimeSlotService.setDaysToCache(3);
    }

    @Test
    public void test_cacheTimeSlots() {
        availableTimeSlotService.cacheTimeSlots();

        //verify
        verify(availableTimeSlotsRepo, times(1)).deleteAll();
    }

    @Test
    public void test_cacheTimeSlotsByDate() {
        //prepare
        List<VAvailableTimeSlot> availableTimeSlots = new ArrayList<>();
        availableTimeSlots.add(new VAvailableTimeSlot("12:00", "1", "1,2"));
        availableTimeSlots.add(new VAvailableTimeSlot("11:05", "1", "1,2"));

        when(vAvailableTimeSlotRepo.findAllFreeTimeSlotsByDate(any(LocalDate.class)))
                .thenReturn(availableTimeSlots);

        Optional<TimeSlot> optionalTimeSlot1 = Optional.of(new TimeSlot());
        when(timeSlotRepo.findByTime("12:00")).thenReturn(optionalTimeSlot1);

        Optional<TimeSlot> optionalTimeSlot2 = Optional.empty();
        when(timeSlotRepo.findByTime("11:05")).thenReturn(optionalTimeSlot2);

        //call
        availableTimeSlotService.cacheTimeSlotsByDate(LocalDate.now());

        //verify
        verify(vAvailableTimeSlotRepo, times(1)).findAllFreeTimeSlotsByDate(any(LocalDate.class));
        verify(availableTimeSlotsRepo, times(1)).save(any(AvailableTimeSlot.class));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(timeSlotRepo, times(2)).findByTime(captor.capture());

        List<String> values = captor.getAllValues();
        Assert.assertEquals("12:00", values.get(0));
        Assert.assertEquals("11:05", values.get(1));
    }
}