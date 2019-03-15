package com.outfittery.challenge.repositories;

import com.outfittery.challenge.models.Customer;
import com.outfittery.challenge.models.Reservation;
import com.outfittery.challenge.models.Stylist;
import com.outfittery.challenge.models.TimeSlot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReservationRepoTest {
    @Autowired
    private ReservationRepo reservationRepo;

    @Autowired
    private StylistRepo stylistRepo;

    @Autowired
    private TimeSlotRepo timeSlotRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Before
    public void init() {
        TimeSlot timeSlot = timeSlotRepo.findByTime("11:00").get();
        Stylist stylist1 = stylistRepo.getOne(1L);
        Customer customer1 = customerRepo.getOne(1L);
        Customer customer2 = customerRepo.getOne(2L);
        Reservation reservation = new Reservation();
        reservation.setDate(LocalDate.now().plusDays(4));
        reservation.setStylist(stylist1);
        reservation.setTimeSlot(timeSlot);
        reservation.setCustomer(customer1);
        reservationRepo.save(reservation);

        reservation = new Reservation();
        reservation.setDate(LocalDate.now().plusDays(6));
        reservation.setStylist(stylist1);
        reservation.setTimeSlot(timeSlot);
        reservation.setCustomer(customer2);
        reservationRepo.save(reservation);
    }

    @Test
    public void findByStylistIdAndDateBetween() {
        List<Reservation> reservations = reservationRepo.findByStylistIdAndDateBetween(1L, LocalDate.now().plusDays(3), LocalDate.now().plusDays(7));
        assertNotNull(reservations);
        assertEquals(2, reservations.size());
    }

    @Test
    public void findByStylistIdAndDateGreaterThanEqual() {
        List<Reservation> reservations = reservationRepo.findByStylistIdAndDateGreaterThanEqual(1L, LocalDate.now().plusDays(1));
        assertNotNull(reservations);
        assertEquals(2, reservations.size());
    }

    @Test
    public void findByCustomerIdAndDateGreaterThanEqual() {
        List<Reservation> reservations = reservationRepo.findByCustomerIdAndDateGreaterThanEqual(2L, LocalDate.now().plusDays(5));
        assertNotNull(reservations);
        assertEquals(1, reservations.size());
    }
}