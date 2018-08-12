package com.outfittery.challenge.controllers;

import com.outfittery.challenge.models.Customer;
import com.outfittery.challenge.models.Leave;
import com.outfittery.challenge.models.Stylist;
import com.outfittery.challenge.rest.dto.CustomerResponse;
import com.outfittery.challenge.services.CustomerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1.0/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @ApiOperation(value = "View a list of customers", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list")
    }
    )
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerResponse> getAll() {
        return customerService.getAll();
    }

    @ApiOperation(value = "View customer by id", response = CustomerResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "Requested id not found")
    }
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse getById(@Positive(message = "should be positive non zero value")
                                    @PathVariable(name = "id")
                                            Long id) {
        return customerService.getById(id);
    }

    @ApiOperation(value = "Add new or update customer", response = CustomerResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created customer"),
            @ApiResponse(code = 400, message = "Incorrect customer entered")
    }
    )
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @ApiOperation(value = "Delete customer")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully created customer"),
            @ApiResponse(code = 400, message = "Incorrect customer id entered")
    }
    )
    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@Positive(message = "should be positive non zero value")
                               @PathVariable(name = "id")
                                       Long id) {
        customerService.delete(id);
    }
}
