package com.outfittery.challenge.controllers;

import com.outfittery.challenge.models.Leave;
import com.outfittery.challenge.models.Stylist;
import com.outfittery.challenge.models.StylistState;
import com.outfittery.challenge.rest.dto.LeaveRequest;
import com.outfittery.challenge.services.StylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("api/v1/stylist")
public class StylistController {
    @Autowired
    private StylistService stylistService;

    @PostMapping("/request-for-leave")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean requestForLeave(@Valid @RequestBody LeaveRequest request) {
        return stylistService.requestForLeave(request);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Stylist createStylist(@Valid @RequestBody Stylist stylist) {
        return stylistService.save(stylist);
    }

    @PatchMapping("/ready-for-style/{stylistId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void readyForStyle(@Positive(message = "should be positive non zero value")
                              @PathVariable(name = "stylistId")
                              Long stylistId) {
        stylistService.updateState(stylistId);
    }
}
