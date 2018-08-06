package com.outfittery.challenge.services;

import com.outfittery.challenge.models.Customer;
import com.outfittery.challenge.rest.dto.CustomerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CustomerService {

    List<CustomerResponse> getAll();

    CustomerResponse getById(Long id);

    CustomerResponse save(Customer customer);

    void delete(Long id);
}
