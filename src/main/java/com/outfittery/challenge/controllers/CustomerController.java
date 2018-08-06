package com.outfittery.challenge.controllers;

import com.outfittery.challenge.models.Customer;
import com.outfittery.challenge.models.Leave;
import com.outfittery.challenge.models.Stylist;
import com.outfittery.challenge.rest.dto.CustomerResponse;
import com.outfittery.challenge.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerResponse> getAll() {
        //todo implement validation
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse getById(@PathVariable(name = "id") Long id) {
        //todo implement validation
        return customerService.getById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@RequestBody Customer customer) {
        //todo implement validation
        return customerService.save(customer);
    }

    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteCustomer(@PathVariable(name = "id") Long id) {
        //todo implement validation and return type
        customerService.delete(id);
        return "ok";
    }
}
