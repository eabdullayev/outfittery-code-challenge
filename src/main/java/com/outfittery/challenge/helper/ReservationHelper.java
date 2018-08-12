package com.outfittery.challenge.helper;

import com.outfittery.challenge.models.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Helper class to operate over Reservation
 */
public class ReservationHelper {
    /**
     * check if customer has reservation from now
     * @param customerReservations
     * @return future reservation
     */
    public static Reservation hasBooking(List<Reservation> customerReservations) {
        for (Reservation r : customerReservations) {
            if (r.getDate().isAfter(LocalDate.now()) ||
                    (LocalDate.now().isEqual(r.getDate()) && LocalTime.now().isBefore(LocalTime.parse(r.getTimeSlot().getTime())))) {

                return r;
            }
        }
        return null;
    }
}
