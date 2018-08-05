package com.outfittery.challenge.controllers;

import com.outfittery.challenge.models.Leave;
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
    public boolean makeReservation(@RequestBody Leave leave) {
        //todo implement validation
        return stylistService.requestForLeave(leave);
    }
}
