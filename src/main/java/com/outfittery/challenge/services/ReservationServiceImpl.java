package com.outfittery.challenge.services;

import com.outfittery.challenge.exceptions.BadRequestException;
import com.outfittery.challenge.exceptions.GenericException;
import com.outfittery.challenge.exceptions.ResourceNotFoundException;
import com.outfittery.challenge.helper.ReservationHelper;
import com.outfittery.challenge.helper.StylistHelper;
import com.outfittery.challenge.models.*;
import com.outfittery.challenge.repositories.*;
import com.outfittery.challenge.rest.dto.ManyReservationResponse;
import com.outfittery.challenge.rest.dto.ReservationRequest;
import com.outfittery.challenge.rest.dto.ReservationResponse;
import com.outfittery.challenge.rest.dto.builder.ReservationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Autowired
    private ReservationRepo reservationRepo;

    @Autowired
    private VAvailableTimeSlotRepo vAvailableTimeSlotRepo;

    @Autowired
    private AvailableTimeSlotsRepo availableTimeSlotsRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private TimeSlotRepo timeSlotRepo;

    @Autowired
    private StylistRepo stylistRepo;

    @Autowired
    private AvailableTimeSlotService availableTimeSlotService;

    @Value("${days.to.cache}")
    private Integer daysToCache;

    /**
     * Makes reservation
     * @param reservationRequest
     * @return reservationResponse
     */
    @Override
    @Transactional
    public ReservationResponse makeReservation(ReservationRequest reservationRequest) {
        logger.info("Make reservation, reservatinRequest: " + reservationRequest);
        //db validation start
        Customer customer = customerRepo.findById(reservationRequest.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found", reservationRequest.getCustomerId()));

        List<Reservation> customerReservations = reservationRepo.findByCustomerIdAndDateGreaterThanEqual(customer.getId(), LocalDate.now());

        Reservation r = null;
        if ((r = ReservationHelper.hasBooking(customerReservations)) != null) {
            throw new BadRequestException("You already have reservation at " + r.getDate() + " " + r.getTimeSlot().getTime(),
                    reservationRequest.getDate() + "-" + reservationRequest.getTimeSlot());
        }


        TimeSlot timeSlot = timeSlotRepo.findByTime(reservationRequest.getTimeSlot())
                .orElseThrow(() -> new BadRequestException("Invalid time entered", reservationRequest.getTimeSlot()));

        AvailableTimeSlot availableTimeSlot = getAvailableTimeSlot(reservationRequest.getDate(), timeSlot);

        //db validation end, preparing required data.
        //removing one available stylist from list
        Stylist stylist = stylistRepo.getOne(availableTimeSlot.getAvailableStylists().remove(0));

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setStylist(stylist);
        reservation.setDate(reservationRequest.getDate());
        reservation.setTimeSlot(timeSlot);
        reservationRepo.save(reservation);
        //persisting new available stylist list to db
        availableTimeSlotsRepo.save(availableTimeSlot);

        return ReservationBuilder.buildReservationResponse(reservation);
    }

    /**
     * update reservation date.
     * @param reservationRequest
     * @return - returns updated reservation
     */
    @Override @Transactional
    public ReservationResponse updateReservation(ReservationRequest reservationRequest) {
        logger.info("Update reservation, reservatinRequest: " + reservationRequest);
        if (reservationRequest.getReservationId() == null) {
            throw new BadRequestException("reservation not selected.", reservationRequest.getReservationId());
        }

        Reservation r = reservationRepo.findById(reservationRequest.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found", reservationRequest.getReservationId()));

        //chech if reservation going to be updated is passed or not.
        if (r.getDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Old reservations can not be changed", r.getDate());
        }

        //reservation customer info can not be updated
        if (r.getCustomer().getId() != reservationRequest.getCustomerId()) {
            throw new BadRequestException("invalid operation. reservation not belong to customer", reservationRequest.getCustomerId());
        }

        TimeSlot newTimeSlot = timeSlotRepo.findByTime(reservationRequest.getTimeSlot())
                .orElseThrow(() -> new BadRequestException("Invalid time entered", reservationRequest.getTimeSlot()));
        //if date and time same. halt process
        if (r.getDate().isEqual(reservationRequest.getDate()) && r.getTimeSlot().getTime().equals(reservationRequest.getTimeSlot())) {
            throw new BadRequestException("Nothing to update");
        }

        //checking availability of stylists
        AvailableTimeSlot availableTimeSlot = getAvailableTimeSlot(reservationRequest.getDate(), newTimeSlot);

        //get old available time slot data
        AvailableTimeSlot oldAvailableTimeSlot = availableTimeSlotsRepo.findByDateAndTimeSlot(r.getDate(), r.getTimeSlot())
                .orElseThrow(() -> new BadRequestException("Time slot not available.", reservationRequest.getDate() + "-" + reservationRequest.getTimeSlot()));
        //add old stylist to available list and save
        oldAvailableTimeSlot.getAvailableStylists().add(r.getStylist().getId());
        availableTimeSlotsRepo.save(oldAvailableTimeSlot);

        //get new stylist for new date and time.
        Stylist stylist = stylistRepo.getOne(availableTimeSlot.getAvailableStylists().remove(0));

        r.setStylist(stylist);
        r.setDate(reservationRequest.getDate());
        r.setTimeSlot(newTimeSlot);
        reservationRepo.save(r);

        //update available list for stylist
        availableTimeSlotsRepo.save(availableTimeSlot);

        return ReservationBuilder.buildReservationResponse(r);
    }

    /**
     * Used for adding list of reservations to db. If any of element unable to add db will be skipped
     * and add to respon`s failed list.
     * @param reservationRequestList - list of reservations to add db
     * @return ManyReservationResponse. It has 2 list field: successfully added and failed reservations lists.
     */
    @Override
    @Transactional
    public ManyReservationResponse makeManyReservations(List<ReservationRequest> reservationRequestList) {
        logger.info("add many reservations, number or reservations: " + reservationRequestList.size());
        ManyReservationResponse response = new ManyReservationResponse();
        for (ReservationRequest request : reservationRequestList) {
            try {
                ReservationResponse rr = this.makeReservation(request);
                response.getProcessedReservations().add(rr);
            } catch (RuntimeException re) {
                //if because of any reason will not be able to add db, will be skipped and process will continue for remain.
                logger.warn("reservation: " + request + " failed because of: " + re.getMessage());
                response.getFailedReservations().add(request);
            }
        }
        logger.info("total successful reservations: " + response.getProcessedReservations().size() +
                ", total failed: " + response.getFailedReservations().size());
        return response;
    }

    /**
     * Retruns all free time slots by date. if Date less than cached date then will read from cached data.
     *If more than cached dates then first generate and add new date then use.
     * @param date - date of time slots
     * @return time slots as list of string
     */
    @Override
    public List<String> getTimeSlots(LocalDate date) {
        logger.info("getting time slots for date: " + date);

        List<AvailableTimeSlot> availableTimeSlots = availableTimeSlotsRepo.findAllTimeSlotsByDate(date);
        //if not cached yet, do manual caching for that date
        if (availableTimeSlots == null || availableTimeSlots.size()==0) {
            createCacheForDate(date);
            availableTimeSlots = availableTimeSlotsRepo.findAllTimeSlotsByDate(date);
        }

        return availableTimeSlots.stream()
                .map(availableTimeSlot -> availableTimeSlot.getTimeSlot().getTime())
                .collect(Collectors.toList());
    }


    /**
     * manually creating cache for missing dates
     * @param date
     */
    private void createCacheForDate(LocalDate date) {
        logger.info("manual generating time slots for date: " + date);
        availableTimeSlotService.cacheTimeSlotsByDate(date);
    }

    /**
     * Check date if cache exist, if not create and send back AvailableTimeSlot.
     * @param date
     * @param timeSlot
     * @return
     */
    private AvailableTimeSlot getAvailableTimeSlot(LocalDate date, TimeSlot timeSlot) {
        Optional<AvailableTimeSlot> optional = availableTimeSlotsRepo.findByDateAndTimeSlot(date, timeSlot);

        if(!optional.isPresent()){
            createCacheForDate(date);
            optional = availableTimeSlotsRepo.findByDateAndTimeSlot(date, timeSlot);
        }
        AvailableTimeSlot availableTimeSlot = optional.orElseThrow(() -> new GenericException("Something went wrong!!!"));
        if (availableTimeSlot.getAvailableStylists().size() == 0) {
            throw new BadRequestException("Time slot not available.", date + "-" + timeSlot);
        }

        return availableTimeSlot;
    }
}
