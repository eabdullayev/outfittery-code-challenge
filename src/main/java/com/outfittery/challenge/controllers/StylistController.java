package com.outfittery.challenge.controllers;

import com.outfittery.challenge.models.Leave;
import com.outfittery.challenge.models.Stylist;
import com.outfittery.challenge.models.StylistState;
import com.outfittery.challenge.rest.dto.LeaveRequest;
import com.outfittery.challenge.rest.dto.ReservationResponse;
import com.outfittery.challenge.services.StylistService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("api/v1.0/stylist")
public class StylistController {
    @Autowired
    private StylistService stylistService;

    @ApiOperation(value = "Request for leave", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created leave for stylist"),
            @ApiResponse(code = 400, message = "Incorrect data entered"),
            @ApiResponse(code = 404, message = "Entered data not found in DB")
    }
    )
    @PostMapping("/request-for-leave")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean requestForLeave(@Valid @RequestBody LeaveRequest request) {
        return stylistService.requestForLeave(request);
    }

    @ApiOperation(value = "Add new or update existing stylist", response = Stylist.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created leave for stylist"),
            @ApiResponse(code = 400, message = "Incorrect data entered")
    }
    )
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Stylist createStylist(@Valid @RequestBody Stylist stylist) {
        return stylistService.save(stylist);
    }

    @ApiOperation(value = "Update stylist to ready for style")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created leave for stylist"),
            @ApiResponse(code = 400, message = "Incorrect data entered"),
            @ApiResponse(code = 404, message = "Entered data not found in DB")
    }
    )
    @PatchMapping("/ready-for-style/{stylistId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void readyForStyle(@Positive(message = "should be positive non zero value")
                              @PathVariable(name = "stylistId")
                              Long stylistId) {
        stylistService.updateState(stylistId);
    }
}
