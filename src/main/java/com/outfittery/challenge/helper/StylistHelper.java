package com.outfittery.challenge.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StylistHelper {
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

    public static List<Long> getAvailableStylists(String busyStylists, String freeStylists){
        List<String> freeStylistList = Arrays.asList(freeStylists.split(","));
        Set<Long> freeStylistSet = freeStylistList.stream()
                .mapToLong(Long::parseLong).boxed().collect(Collectors.toSet());

        if(busyStylists != null) {
            List<String> busyStylistList = Arrays.asList(busyStylists.split(","));
            Set<Long> busyStylistSet = busyStylistList.stream()
                    .mapToLong(Long::parseLong).boxed().collect(Collectors.toSet());

            freeStylistSet.removeAll(busyStylistSet);
        }

        return new ArrayList<>(freeStylistSet);
    }
}
