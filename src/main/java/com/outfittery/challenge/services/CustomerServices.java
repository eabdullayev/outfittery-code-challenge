package com.outfittery.challenge.services;

import com.outfittery.challenge.models.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerServices {

    Customer getById(Long id);

    void save(Customer customer);

    Customer update(Customer customer);

    boolean delete(Long id);
}
