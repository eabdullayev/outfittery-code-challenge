package com.outfittery.challenge.helper;

import com.outfittery.challenge.models.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReservationHelper {
    public static Long getFreeStylist(String busyStylists, String freeStylists){

        List<String> freeStylistList = Arrays.asList(freeStylists.split(","));
        Set<Long> freeStylistSet = freeStylistList.stream()
                .mapToLong(Long::parseLong).boxed().collect(Collectors.toSet());

        if(busyStylists != null) {
            List<String> busyStylistList = Arrays.asList(busyStylists.split(","));
            Set<Long> busyStylistSet = busyStylistList.stream()
                    .mapToLong(Long::parseLong).boxed().collect(Collectors.toSet());

            freeStylistSet.removeAll(busyStylistSet);
        }

        return freeStylistSet.iterator().next();
    }

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
