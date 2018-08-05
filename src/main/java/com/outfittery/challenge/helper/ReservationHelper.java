package com.outfittery.challenge.helper;

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
}
