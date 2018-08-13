package com.outfittery.challenge.services;

import com.outfittery.challenge.exceptions.BadRequestException;
import com.outfittery.challenge.exceptions.GenericException;
import com.outfittery.challenge.exceptions.ResourceNotFoundException;
import com.outfittery.challenge.models.*;
import com.outfittery.challenge.repositories.*;
import com.outfittery.challenge.rest.dto.ReservationRequest;
import com.outfittery.challenge.rest.dto.ReservationResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceImplTest {
    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepo reservationRepo;

    @Mock
    private VAvailableTimeSlotRepo vAvailableTimeSlotRepo;

    @Mock
    private AvailableTimeSlotsRepo availableTimeSlotsRepo;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private TimeSlotRepo timeSlotRepo;

    @Mock
    private StylistRepo stylistRepo;

    @Mock
    private AvailableTimeSlotService availableTimeSlotService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        //init customerRepo.findById
        Customer customer = mock(Customer.class);
        Optional<Customer> optionalCustomer = Optional.of(customer);
        when(customerRepo.findById(anyLong())).thenReturn(optionalCustomer);

        //init reservationRepo.findByCustomerIdAndDateGreaterThanEqual
        List<Reservation> reservationList = new ArrayList<>();
        when(reservationRepo.findByCustomerIdAndDateGreaterThanEqual(anyLong(), any(LocalDate.class)))
                .thenReturn(reservationList);

        //init TimeSlot
        TimeSlot timeSlot = mock(TimeSlot.class);
        when(timeSlot.getTime()).thenReturn("12:00");

        //init timeSlotRepo.findByTime
        Optional<TimeSlot> timeSlotOptional = Optional.of(timeSlot);
        when(timeSlotRepo.findByTime(anyString())).thenReturn(timeSlotOptional);

        //init availableTimeSlot
        AvailableTimeSlot availableTimeSlot = mock(AvailableTimeSlot.class);
        List<Long> availableStylist = mock(List.class);
        when(availableStylist.size()).thenReturn(1);
        when(availableStylist.remove(anyInt())).thenReturn(1L);
        when(availableTimeSlot.getAvailableStylists()).thenReturn(availableStylist);

        //init availableTimeSlotsRepo.findByDateAndTimeSlot
        Optional<AvailableTimeSlot> optionalAvailableTimeSlot = Optional.of(availableTimeSlot);
        when(availableTimeSlotsRepo.findByDateAndTimeSlot(any(LocalDate.class), any(TimeSlot.class)))
                .thenReturn(optionalAvailableTimeSlot);

        //init stylistRepo.getOne
        Stylist stylist = mock(Stylist.class);
        when(stylistRepo.getOne(anyLong())).thenReturn(stylist);

        //init reservationRepo.findById
        when(customer.getId()).thenReturn(1L);
        Reservation reservation = mock(Reservation.class);
        when(reservation.getDate()).thenReturn(LocalDate.now().plusDays(1));
        when(reservation.getTimeSlot()).thenReturn(timeSlot);
        when(reservation.getCustomer()).thenReturn(customer);
        when(reservation.getStylist()).thenReturn(stylist);

        Optional<Reservation> reservationOptional = Optional.of(reservation);
        when(reservationRepo.findById(anyLong())).thenReturn(reservationOptional);
    }

    @Test
    public void makeReservation_ok() {
        //preparation
        ReservationRequest reservationRequest = mock(ReservationRequest.class);
        when(reservationRequest.getTimeSlot()).thenReturn("11:00");
        when(reservationRequest.getDate()).thenReturn(LocalDate.now());

        //call
        ReservationResponse reservationResponse = reservationService.makeReservation(reservationRequest);

        //verify
        verify(customerRepo, times(1)).findById(anyLong());
        verify(reservationRepo, times(1)).findByCustomerIdAndDateGreaterThanEqual(anyLong(), any(LocalDate.class));
        verify(timeSlotRepo, times(1)).findByTime(anyString());
        verify(availableTimeSlotsRepo, times(1)).findByDateAndTimeSlot(any(LocalDate.class), any(TimeSlot.class));
        verify(stylistRepo, times(1)).getOne(anyLong());
        verify(reservationRepo, times(1)).save(any(Reservation.class));
        verify(availableTimeSlotsRepo, times(1)).save(any(AvailableTimeSlot.class));

        assertEquals("12:00", reservationResponse.getTimeSlot());
    }

    @Test
    public void makeReservation_customerNotFound() {
        //preparation
        expectedException.expect(ResourceNotFoundException.class);
        expectedException.expectMessage("Customer not found");
        Optional<Customer> optionalCustomer = Optional.empty();
        when(customerRepo.findById(anyLong())).thenReturn(optionalCustomer);

        ReservationRequest reservationRequest = mock(ReservationRequest.class);

        //call
        ReservationResponse reservationResponse = reservationService.makeReservation(reservationRequest);

        //verify
        verify(customerRepo, times(1)).findById(anyLong());
    }

    @Test
    public void makeReservation_alreadyHasBooking() {
        //preparation
        expectedException.expect(BadRequestException.class);

        TimeSlot timeSlot = mock(TimeSlot.class);
        when(timeSlot.getTime()).thenReturn("12:00");

        Reservation reservation = mock(Reservation.class);
        when(reservation.getDate()).thenReturn(LocalDate.now().plusDays(1));
        when(reservation.getTimeSlot()).thenReturn(timeSlot);
        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);
        when(reservationRepo.findByCustomerIdAndDateGreaterThanEqual(anyLong(), any(LocalDate.class)))
                .thenReturn(reservationList);

        ReservationRequest reservationRequest = mock(ReservationRequest.class);

        //call
        ReservationResponse reservationResponse = reservationService.makeReservation(reservationRequest);

        //verify
        verify(customerRepo, times(1)).findById(anyLong());
        verify(reservationRepo, times(1)).findByCustomerIdAndDateGreaterThanEqual(anyLong(), any(LocalDate.class));
    }

    @Test
    public void makeReservation_invalidTimeSlot() {
        //preparation
        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("Invalid time entered");

        ReservationRequest reservationRequest = mock(ReservationRequest.class);

        //call
        ReservationResponse reservationResponse = reservationService.makeReservation(reservationRequest);

        //verify
        verify(timeSlotRepo, times(1)).findByTime(anyString());
    }

    @Test
    public void makeReservation_noAvailableTimeSlot() {
        //preparation
        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("Time slot not available.");

        AvailableTimeSlot availableTimeSlot = mock(AvailableTimeSlot.class);
        List<Long> availableStylist = mock(List.class);
        when(availableStylist.size()).thenReturn(0);
        when(availableTimeSlot.getAvailableStylists()).thenReturn(availableStylist);

        Optional<AvailableTimeSlot> optionalAvailableTimeSlot = Optional.of(availableTimeSlot);
        when(availableTimeSlotsRepo.findByDateAndTimeSlot(any(LocalDate.class), any(TimeSlot.class)))
                .thenReturn(optionalAvailableTimeSlot);

        ReservationRequest reservationRequest = mock(ReservationRequest.class);
        when(reservationRequest.getTimeSlot()).thenReturn("11:00");
        when(reservationRequest.getDate()).thenReturn(LocalDate.now());

        //call
        ReservationResponse reservationResponse = reservationService.makeReservation(reservationRequest);

        //verify
        verify(availableTimeSlotsRepo, times(1)).findByDateAndTimeSlot(any(LocalDate.class), any(TimeSlot.class));
    }

    @Test
    public void makeReservation_genericException() {
        //preparation
        expectedException.expect(GenericException.class);
        expectedException.expectMessage("Something went wrong!!!");

        Optional<AvailableTimeSlot> optionalAvailableTimeSlot = Optional.empty();
        when(availableTimeSlotsRepo.findByDateAndTimeSlot(any(LocalDate.class), any(TimeSlot.class)))
                .thenReturn(optionalAvailableTimeSlot);

        ReservationRequest reservationRequest = mock(ReservationRequest.class);
        when(reservationRequest.getTimeSlot()).thenReturn("11:00");
        when(reservationRequest.getDate()).thenReturn(LocalDate.now());

        //call
        ReservationResponse reservationResponse = reservationService.makeReservation(reservationRequest);

        //verify
        verify(availableTimeSlotsRepo, times(1)).findByDateAndTimeSlot(any(LocalDate.class), any(TimeSlot.class));
    }

    @Test
    public void updateReservation_ok() {
        //preparation
        ReservationRequest reservationRequest = mock(ReservationRequest.class);
        when(reservationRequest.getReservationId()).thenReturn(1L);
        when(reservationRequest.getTimeSlot()).thenReturn("11:00");
        when(reservationRequest.getDate()).thenReturn(LocalDate.now());
        when(reservationRequest.getCustomerId()).thenReturn(1L);

        //call
        ReservationResponse reservationResponse = reservationService.updateReservation(reservationRequest);

        //verify
        verify(reservationRepo, times(1)).findById(anyLong());
        verify(timeSlotRepo, times(1)).findByTime(anyString());
        verify(availableTimeSlotsRepo, times(2)).findByDateAndTimeSlot(any(LocalDate.class), any(TimeSlot.class));
        verify(stylistRepo, times(1)).getOne(anyLong());
        verify(reservationRepo, times(1)).save(any(Reservation.class));
        verify(availableTimeSlotsRepo, times(2)).save(any(AvailableTimeSlot.class));
    }

    @Test
    public void updateReservation_reservationNotSelected(){
        //preparation
        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("reservation not selected.");
        ReservationRequest reservationRequest = mock(ReservationRequest.class);
        when(reservationRequest.getReservationId()).thenReturn(null);

        //call
        ReservationResponse reservationResponse = reservationService.updateReservation(reservationRequest);
    }

    @Test
    public void updateReservation_oldReservation(){
        //preparation
        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("Old reservations can not be changed");
        Reservation reservation = mock(Reservation.class);
        when(reservation.getDate()).thenReturn(LocalDate.now().minusDays(1));
        Optional<Reservation> reservationOptional = Optional.of(reservation);
        when(reservationRepo.findById(anyLong())).thenReturn(reservationOptional);

        ReservationRequest reservationRequest = mock(ReservationRequest.class);

        //call
        ReservationResponse reservationResponse = reservationService.updateReservation(reservationRequest);

        //verify
        verify(reservationRepo, times(1)).findById(anyLong());
    }

    @Test
    public void updateReservation_notBelongToCustomer(){
        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("invalid operation. reservation not belong to customer");
        Reservation reservation = mock(Reservation.class);
        when(reservation.getDate()).thenReturn(LocalDate.now().plusDays(1));
        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(5L);
        when(reservation.getCustomer()).thenReturn(customer);
        Optional<Reservation> reservationOptional = Optional.of(reservation);
        when(reservationRepo.findById(anyLong())).thenReturn(reservationOptional);

        ReservationRequest reservationRequest = mock(ReservationRequest.class);

        //call
        ReservationResponse reservationResponse = reservationService.updateReservation(reservationRequest);

        //verify
        verify(reservationRepo, times(1)).findById(anyLong());
    }

    @Test
    public void makeManyReservations() {
    }

    @Test
    public void getTimeSlots() {
    }
}