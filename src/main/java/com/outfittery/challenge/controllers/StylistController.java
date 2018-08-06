package com.outfittery.challenge.controllers;

import com.outfittery.challenge.models.Leave;
import com.outfittery.challenge.models.Stylist;
import com.outfittery.challenge.models.StylistState;
import com.outfittery.challenge.services.StylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/stylist")
public class StylistController {
    @Autowired
    private StylistService stylistService;

    @PostMapping("/request-for-leave")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean requestForLeave(@RequestBody Leave leave) {
        //todo implement validation
        return stylistService.requestForLeave(leave);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Stylist createStylist(@RequestBody Stylist stylist) {
        //todo implement validation
        return stylistService.save(stylist);
    }

    @PatchMapping("/ready-for-style/{stylistId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Long updateState(@PathVariable(name = "stylistId") Long stylistId) {
        //todo implement validation
        return stylistService.updateState(stylistId);
    }
}
