package com.outfittery.challenge.controllers;

import com.outfittery.challenge.models.Customer;
import com.outfittery.challenge.models.Leave;
import com.outfittery.challenge.models.Stylist;
import com.outfittery.challenge.rest.dto.CustomerResponse;
import com.outfittery.challenge.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerResponse> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse getById(@Positive(message = "should be positive non zero value")
                                    @PathVariable(name = "id")
                                    Long id) {
        return customerService.getById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@Positive(message = "should be positive non zero value")
                                 @PathVariable(name = "id")
                                 Long id) {
        customerService.delete(id);
    }
}
