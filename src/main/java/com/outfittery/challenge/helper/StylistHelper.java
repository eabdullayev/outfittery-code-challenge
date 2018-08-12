package com.outfittery.challenge.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Helper class to operate over stylist.
 */
public class StylistHelper {

    /**
     * subtract from allStylist list busy stylist list and return back free stylist list
     * @param busyStylists
     * @param allStylist
     * @return
     */
    public static List<Long> getAvailableStylists(String busyStylists, String allStylist){
        List<String> allStylistList = Arrays.asList(allStylist.split(","));
        Set<Long> allStylistSet = allStylistList.stream()
                .mapToLong(Long::parseLong).boxed().collect(Collectors.toSet());

        if(busyStylists != null) {
            List<String> busyStylistList = Arrays.asList(busyStylists.split(","));
            Set<Long> busyStylistSet = busyStylistList.stream()
                    .mapToLong(Long::parseLong).boxed().collect(Collectors.toSet());

            allStylistSet.removeAll(busyStylistSet);
        }

        return new ArrayList<>(allStylistSet);
    }
}
